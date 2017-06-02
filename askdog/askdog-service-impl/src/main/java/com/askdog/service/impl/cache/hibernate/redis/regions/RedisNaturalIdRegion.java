package com.askdog.service.impl.cache.hibernate.redis.regions;

import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisNaturalIdRegion extends RedisTransactionalDataRegion implements NaturalIdRegion {

    public RedisNaturalIdRegion(RedisAccessStrategyFactory accessStrategyFactory,
                                RedisTemplate<String, Object> template,
                                String regionName,
                                Settings settings,
                                CacheDataDescription metadata) {
        super(accessStrategyFactory, template, regionName, settings, metadata);
    }

    @Override
    public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createNaturalIdRegionAccessStrategy(this, accessType);
    }
}
