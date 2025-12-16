package lld;
/*
    ref : https://chatgpt.com/share/6921c0e1-0644-800e-9bc6-d7917e6c1ffd
    - Allow up to N requests per time unit (e.g., 100 requests/minute) per key.
    - Reject or queue requests exceeding limit.
    - Configurable per key, per endpoint, per plan.

    decisions to call out
    - Hard vs soft limit (hard = reject immediately; soft = queue/throttle).
    - Burst behavior allowed? (token bucket allows bursts)
    - Sliding window vs fixed window (accuracy vs memory).

    User ID
        - Perfect for authenticated APIs. Each logged-in user gets their own rate limit allocation. This is typically present in the Authorization header as a JWT token.
    IP Address
        - Good for public APIs or when you don't have user accounts. But watch out for users behind NATs or corporate firewalls. The IP address is typically present in the X-Forwarded-For header.
    API Key
        - Common for developer APIs. Each keyholder gets their own limits. Most typically, this is denoted in the X-API-Key header.

    ===points===
    - Use redis for distributed cache
    - write token-bucket code in redis lua script

    ====================Fixed Window counter=======================
    public boolean allow() {
        long now = System.currentTimeMillis();

        if (now - windowStart >= windowSize) {
            windowStart = now;
            counter = 0;
        }

        if (counter < limit) {
            counter++;
            return true;
        }
        return false;
    }

    ====================Sliding Window Log=======================
    public boolean allow() {
        long now = System.currentTimeMillis();

        while (!timestamps.isEmpty() && timestamps.peekFirst() <= now - windowSize) {
            timestamps.pollFirst();
        }

        if (timestamps.size() < limit) {
            timestamps.addLast(now);
            return true;
        }
        return false;
    }

    ====================Sliding Window Counter=======================
    public boolean allow() {
        long now = System.currentTimeMillis();
        long diff = now - windowStart;

        if (diff >= bucketSize) {
            previousBucket = currentBucket;
            currentBucket = 0;
            windowStart = now;
        }

        double weight = (double)(bucketSize - diff) / bucketSize;
        double estimated = previousBucket * weight + currentBucket;

        if (estimated < limit) {
            currentBucket++;
            return true;
        }
        return false;
    }

    ====================Token Bucket=======================
    public boolean allow() {
        long now = System.currentTimeMillis();
        double added = ((now - lastRefill) / 1000.0) * refillRate;

        tokens = Math.min(capacity, tokens + added);
        lastRefill = now;

        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    note :
        - suppose refillRate = 10 tokens / sec and capacity = 100
        - and lets say user does nothing for 10 seconds, then tokens collected will be 100
        - user can burst 100 requests and once. but that is allowed.
        - because token bucket, allows short bursts not bigger bursts

    ====================Leaky Bucket=======================
    public boolean allow() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastLeakTime;

        long leaks = elapsed / leakIntervalMillis; // how many units leaked

        current = Math.max(0, current - (int) leaks);
        lastLeakTime = now;

        if (current < capacity) {
            current++;
            return true;
        }
        return false;
    }

    note :
        - allows bursts but up to capacity
*/

public class RateLimiter {

}
