package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisTransactionalDataRegion;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

abstract class AbstractRedisAccessStrategy<T extends RedisTransactionalDataRegion> {

    protected final T region;
    protected final Settings settings;

    AbstractRedisAccessStrategy(T region, Settings settings) {
        this.region = region;
        this.settings = settings;
    }

    protected Settings settings() {
        return settings;
    }

    public final boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) {
        return putFromLoad(key, value, txTimestamp, version, settings.isMinimalPutsEnabled());
    }

    public abstract boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride);

    /**
     * Region locks are not supported
     */
    public final SoftLock lockRegion() {
        return null;
    }

    /**
     * Region locks are not supported - perform a cache clear as a precaution.
     *
     * @param lock soft lock instance
     */
    public final void unlockRegion(SoftLock lock) {
        region.clear();
    }

    /**
     * A no-op since this is an asynchronous cache access strategy.
     *
     * @param key key
     */
    public void remove(Object key) { }

    /**
     * Called to evict data from the entire region
     */
    public final void removeAll() {
        region.clear();
    }

    /**
     * Remove the given mapping without regard to transactional safety
     *
     * @param key key
     */
    public final void evict(Object key) {
        region.remove(key);
    }

    /**
     * Remove all mappings without regard to transactional safety
     */
    public final void evictAll() {
        region.clear();
    }
}
