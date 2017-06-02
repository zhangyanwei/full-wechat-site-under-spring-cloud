package com.askdog.service.impl.cache.hibernate.redis.regions;

import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cfg.Settings;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static java.lang.String.valueOf;

public class RedisTransactionalDataRegion extends RedisDataRegion implements TransactionalDataRegion {

    private final Settings settings;

    private final CacheDataDescription metadata;

    RedisTransactionalDataRegion(RedisAccessStrategyFactory accessStrategyFactory,
                                 RedisTemplate<String, Object> template,
                                 String regionName,
                                 Settings settings,
                                 CacheDataDescription metadata) {
        super(accessStrategyFactory, template, regionName);

        this.settings = settings;
        this.metadata = metadata;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean isTransactionAware() {
        return false;
    }

    @Override
    public CacheDataDescription getCacheDataDescription() {
        return metadata;
    }

    public Object get(Object key) {
        HashOperations<String, String, Object> hashOperations = getTemplate().opsForHash();
        return hashOperations.get(getName(), valueOf(key));
    }


    public void put(Object key, Object value) {
        HashOperations<String, String, Object> hashOperations = getTemplate().opsForHash();
        hashOperations.put(getName(), valueOf(key), value);
    }

    public void remove(Object key) throws CacheException {
        HashOperations<String, String, Object> hashOperations = getTemplate().opsForHash();
        hashOperations.delete(getName(), valueOf(key));
    }


    public void clear() {
        getTemplate().delete(getName());
    }
}
