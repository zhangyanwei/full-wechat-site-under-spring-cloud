package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisTransactionalDataRegion;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

class AbstractReadWriteRedisAccessStrategy<T extends RedisTransactionalDataRegion> extends AbstractRedisAccessStrategy<T> {

    /**
     * Creates a read/write cache access strategy around the given cache region.
     */
    AbstractReadWriteRedisAccessStrategy(T region, Settings settings) {
        super(region, settings);
    }

    /**
     * Returns <code>null</code> if the item is not readable.  Locked items are not readable, nor are items created
     * after the start of this transaction.
     */
    public final Object get(Object key, long txTimestamp) {
        return region.get(key);
    }

    @Override
    public final boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) {
        region.put(key, value);
        return true;
    }

    /**
     * Soft-lock a cache item.
     */
    public final SoftLock lockItem(Object key, Object version) {
        region.remove(key);
        return null;
    }

    /**
     * Soft-unlock a cache item.
     */
    public final void unlockItem(Object key, SoftLock lock) {
        region.remove(key);
    }
}
