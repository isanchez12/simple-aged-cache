// package io.collective;
// import java.time.Clock;

// public class SimpleAgedCache {

//     public SimpleAgedCache(Clock clock) {
//     }

//     public SimpleAgedCache() {
//     }

//     public void put(Object key, Object value, int retentionInMillis) {
//     }

//     public boolean isEmpty() {
//         return false;
//     }

//     public int size() {
//         return 0;
//     }

//     public Object get(Object key) {
//         return null;
//     }
// }

package io.collective;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public class SimpleAgedCache {
    private Map<Object, CacheEntry> cache;
    private Clock clock;

    public SimpleAgedCache(Clock clock) {
        this.cache = new HashMap<>();
        this.clock = clock;
    }

    public SimpleAgedCache() {
        this(Clock.systemDefaultZone());
    }

    public void put(Object key, Object value, int retentionInMillis) {
        long expirationTime = clock.millis() + retentionInMillis;
        CacheEntry entry = new CacheEntry(value, expirationTime);
        cache.put(key, entry);
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public int size() {
        removeExpiredEntries();
        return cache.size();
    }

    public Object get(Object key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired(0)) {
            return entry.getValue();
        }
        return null;
    }

    private void removeExpiredEntries() {
        long currentTime = clock.millis();
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired(currentTime));
    }

    private class CacheEntry {
        private Object value;
        private long expirationTime;

        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired(long currentTime) {
            return currentTime >= expirationTime;
        }
    }
}
