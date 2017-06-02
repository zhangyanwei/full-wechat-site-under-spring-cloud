package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisNaturalIdRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

class ReadWriteRedisNaturalIdRegionAccessStrategy
        extends AbstractReadWriteRedisAccessStrategy<RedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {

    /**
     * Creates a read/write cache access strategy around the given cache region.
     */
    ReadWriteRedisNaturalIdRegionAccessStrategy(RedisNaturalIdRegion region, Settings settings) {
        super(region, settings);
    }

    @Override
    public NaturalIdRegion getRegion() {
        return region;
    }

    @Override
    public boolean insert(Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterInsert(Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean update(Object key, Object value) {
        region.put(key, value);
        return true;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, SoftLock lock) {
        region.put(key, value);
        return true;
    }
}
