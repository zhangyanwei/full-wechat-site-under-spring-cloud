package com.askdog.service.impl.cache.hibernate.redis.regions;

import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import static java.lang.String.valueOf;

abstract class RedisGeneralDataRegion extends RedisDataRegion implements GeneralDataRegion {

    RedisGeneralDataRegion(RedisAccessStrategyFactory accessStrategyFactory,
                           RedisTemplate<String, Object> template,
                           String regionName) {
        super(accessStrategyFactory, template, regionName);
    }

    @Override
    public Object get(Object key) {
        if (key == null) {
            return null;
        }

        HashOperations<String, String, Object> hashOperations = getTemplate().opsForHash();
        return hashOperations.get(getName(), key);
    }

    @Override
    public void put(Object key, Object value) {
        HashOperations<String, String, Object> hashOperations = getTemplate().opsForHash();
        hashOperations.put(getName(), valueOf(key), value);
    }

    @Override
    public void evict(Object key) {
        HashOperations<String, String, Object> hashOperations = getTemplate().opsForHash();
        hashOperations.delete(getName(), key);
    }

    @Override
    public void evictAll() {
        getTemplate().delete(getName());
    }
}
