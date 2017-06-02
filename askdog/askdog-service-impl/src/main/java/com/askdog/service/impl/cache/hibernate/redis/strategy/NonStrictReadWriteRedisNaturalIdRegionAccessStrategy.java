package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisNaturalIdRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NonStrictReadWriteRedisNaturalIdRegionAccessStrategy
        extends AbstractRedisAccessStrategy<RedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {

    private static Logger logger = LoggerFactory.getLogger(NonStrictReadWriteRedisNaturalIdRegionAccessStrategy.class);

    /**
     * Create a non-strict read/write access strategy accessing the given NaturalId region.
     */
    NonStrictReadWriteRedisNaturalIdRegionAccessStrategy(RedisNaturalIdRegion region, Settings settings) {
        super(region, settings);
    }

    @Override
    public NaturalIdRegion getRegion() {
        return region;
    }

    @Override
    public Object get(Object key, long txTimestamp) {
        return region.get(key);
    }

    @Override
    public boolean putFromLoad(Object key,
                               Object value,
                               long txTimestamp,
                               Object version,
                               boolean minimalPutOverride) {
        if (minimalPutOverride && region.contains(key)) {
            return false;
        }
        region.put(key, value);
        return true;
    }

    @Override
    public SoftLock lockItem(Object key, Object version) {
        return null;
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public boolean insert(Object key, Object value) {
        return false;
    }

    @Override
    public boolean afterInsert(Object key, Object value) {
        return false;
    }

    @Override
    public boolean update(Object key, Object value) {
        remove(key);
        return false;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, SoftLock lock) {
        unlockItem(key, lock);
        return false;
    }

    @Override
    public void remove(Object key) {
        logger.trace("remove cache item... key=[{}]", key);
        region.remove(key);
    }
}
