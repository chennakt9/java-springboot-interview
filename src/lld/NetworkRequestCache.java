package lld;

/*
    Request
    - url
    - params (map)
    + getKey()      // create a unique key for cache (url + sorted params)

    Response
    - data
    - timestamp
    - statusCode
    + getData()
    + getStatusCode()

    CacheStore
    - CacheEntry
    - ttl
    + get(key)
    + put(key, CacheEntry)
    + remove(key)
    + contains(key)

    EvictionPolicy
    + onAccess(key)
    + onInsert(key)
    + evict(store)

    LRUEvictionPolicy
    - accessOrder (linked list)
    + onAccess(key)
    + onInsert(key)
    + evict(store)

*/
public class NetworkRequestCache {
}
