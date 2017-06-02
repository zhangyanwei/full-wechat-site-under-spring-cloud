package com.askdog.service.impl.cache.hibernate.redis;

import com.askdog.service.impl.cache.hibernate.redis.regions.*;
import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactoryImpl;
import com.askdog.service.impl.cache.hibernate.redis.util.Timestamper;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Properties;

abstract class AbstractRedisRegionFactory implements RegionFactory {

    private static final long serialVersionUID = -5108693388698702518L;

    private final RedisAccessStrategyFactory accessStrategyFactory = new RedisAccessStrategyFactoryImpl();

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new RedisEntityRegion(accessStrategyFactory, getRedisTemplate(), getRegionName(regionName), getSettings(), metadata);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new RedisNaturalIdRegion(accessStrategyFactory, getRedisTemplate(), getRegionName(regionName), getSettings(), metadata);
    }

    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new RedisCollectionRegion(accessStrategyFactory, getRedisTemplate(), getRegionName(regionName), getSettings(), metadata);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new RedisQueryResultsRegion(accessStrategyFactory, getRedisTemplate(), getRegionName(regionName));
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new RedisTimestampsRegion(accessStrategyFactory, getRedisTemplate(), getRegionName(regionName));
    }

    protected String getRegionName(String regionName) {
        return "cache:hibernate:l2:" + regionName;
    }

    abstract protected Settings getSettings();

    abstract protected RedisTemplate<String, Object> getRedisTemplate();
}
