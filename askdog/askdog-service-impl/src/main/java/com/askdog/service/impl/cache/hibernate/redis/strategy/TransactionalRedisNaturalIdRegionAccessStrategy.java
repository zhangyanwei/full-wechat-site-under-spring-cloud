package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisNaturalIdRegion;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

class TransactionalRedisNaturalIdRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisNaturalIdRegion>
        implements NaturalIdRegionAccessStrategy {

    TransactionalRedisNaturalIdRegionAccessStrategy(RedisNaturalIdRegion region, Settings settings) {
        super(region, settings);
    }

    @Override
    public boolean afterInsert(Object key, Object value) {
        return false;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, SoftLock lock) {
        return false;
    }

    @Override
    public Object get(Object key, long txTimestamp) {
        return region.get(key);
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
    public SoftLock lockItem(Object key, Object version) {
        region.remove(key);
        return null;
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) {
        if (minimalPutOverride && region.contains(key)) {
            return false;
        }
        region.put(key, value);
        return true;
    }


    @Override
    public void remove(Object key) {
        region.remove(key);
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public boolean update(Object key, Object value) {
        region.put(key, value);
        return true;
    }
}
