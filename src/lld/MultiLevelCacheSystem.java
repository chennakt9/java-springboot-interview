package lld;
// ref : https://chatgpt.com/share/6925b2a5-2f28-800e-abfa-df409b646d14

import java.util.*;

/* ---------------------- Cache Interface ---------------------- */

interface Cache {
    String get(String key);
    void put(String key, String value);
}

/* ---------------------- Concrete LRU Cache ---------------------- */

class SimpleCache implements Cache {
    private final int capacity;
    private final LinkedHashMap<String, String> store;

    public SimpleCache(int capacity) {
        this.capacity = capacity;
        this.store = new LinkedHashMap<String, String>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > SimpleCache.this.capacity;
            }
        };
    }

    @Override
    public String get(String key) {
        return store.get(key);
    }

    @Override
    public void put(String key, String value) {
        store.put(key, value);
    }

    @Override
    public String toString() {
        return store.toString();
    }
}

/* ---------------------- Promotion Strategy ---------------------- */

interface PromotionStrategy {
    void promote(String key, String value, CacheHandler handler);
}

/*
   Promote to the immediate parent only:
   L3 → L2
   L2 → L1
   L1 → no promotion
*/
class ImmediateUpperLevelPromotion implements PromotionStrategy {
    @Override
    public void promote(String key, String value, CacheHandler handler) {
        CacheHandler parent = handler.getParent();
        if (parent != null) {
            parent.getCache().put(key, value);
        }
    }
}

/* ---------------------- Chain Handler ---------------------- */

class CacheHandler {
    private final Cache cache;
    private CacheHandler next;
    private CacheHandler parent; // Needed for upward promotion

    public CacheHandler(Cache cache) {
        this.cache = cache;
    }

    public CacheHandler setNext(CacheHandler next) {
        this.next = next;
        next.parent = this;  // Automatically set parent link
        return next;
    }

    public CacheHandler getParent() {
        return parent;
    }

    public Cache getCache() {
        return cache;
    }

    public String get(String key, PromotionStrategy strategy) {
        String value = cache.get(key);

        if (value != null) {
            strategy.promote(key, value, this);
            return value;
        }

        if (next != null) {
            return next.get(key, strategy);
        }

        return null;
    }

    public void put(String key, String value) {
        cache.put(key, value);

        if (next != null) {
            next.put(key, value); // simple write-through
        }
    }
}

/* ---------------------- MultiLevelCache Facade ---------------------- */

class MultiLevelCache {
    private final CacheHandler top;
    private final PromotionStrategy strategy;

    public MultiLevelCache(CacheHandler top, PromotionStrategy strategy) {
        this.top = top;
        this.strategy = strategy;
    }

    public String get(String key) {
        return top.get(key, strategy);
    }

    public void put(String key, String value) {
        top.put(key, value);
    }
}

/* ---------------------- Driver Code ---------------------- */

public class MultiLevelCacheSystem {
    public static void main(String[] args) {

        Cache l1 = new SimpleCache(2);
        Cache l2 = new SimpleCache(3);
        Cache l3 = new SimpleCache(5);

        CacheHandler h1 = new CacheHandler(l1);
        CacheHandler h2 = h1.setNext(new CacheHandler(l2));
        CacheHandler h3 = h2.setNext(new CacheHandler(l3));

        PromotionStrategy strategy = new ImmediateUpperLevelPromotion();

        MultiLevelCache cache = new MultiLevelCache(h1, strategy);

        cache.put("A", "Apple");
        cache.put("B", "Ball");
        cache.put("C", "Cat");

        System.out.println("Get C = " + cache.get("C")); // Should promote L3 → L2
        System.out.println("Get C = " + cache.get("C")); // Should promote L2 → L1

        System.out.println("L1 = " + l1);
        System.out.println("L2 = " + l2);
        System.out.println("L3 = " + l3);
    }
}
