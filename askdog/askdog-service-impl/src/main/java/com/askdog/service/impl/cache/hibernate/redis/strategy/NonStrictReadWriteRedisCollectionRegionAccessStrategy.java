package com.askdog.service.impl.cache.hibernate.redis.strategy;

import com.askdog.service.impl.cache.hibernate.redis.regions.RedisCollectionRegion;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

class NonStrictReadWriteRedisCollectionRegionAccessStrategy extends AbstractRedisAccessStrategy<RedisCollectionRegion>
        implements CollectionRegionAccessStrategy {


    NonStrictReadWriteRedisCollectionRegionAccessStrategy(RedisCollectionRegion region,
                                                          Settings settings) {
        super(region, settings);
    }

    @Override
    public CollectionRegion getRegion() {
        return region;
    }

    @Override
    public Object get(Object key, long txTimestamp) {
        return region.get(key);
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
    public SoftLock lockItem(Object key, Object version) {
        return null;
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) {
        region.remove(key);
    }

    @Override
    public void remove(Object key) {
        region.remove(key);
    }
}
