package com.askdog.service.impl.cache.hibernate.redis.regions;

import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisEntityRegion extends RedisTransactionalDataRegion implements EntityRegion {

    public RedisEntityRegion(RedisAccessStrategyFactory accessStrategyFactory,
                             RedisTemplate<String, Object> template,
                             String regionName,
                             Settings settings,
                             CacheDataDescription metadata) {
        super(accessStrategyFactory, template, regionName, settings, metadata);
    }

    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createEntityRegionAccessStrategy(this, accessType);
    }
}
