package com.askdog.service.impl.cache.hibernate.redis.regions;

import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import com.askdog.service.impl.cache.hibernate.redis.util.Timestamper;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.Region;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

abstract class RedisDataRegion implements Region {

    private static final int DEFAULT_CACHE_LOCK_TIMEOUT = 60 * 1000; // 60 seconds

    private final RedisAccessStrategyFactory accessStrategyFactory;
    private final String name;
    private final RedisTemplate<String, Object> template;

    RedisDataRegion(RedisAccessStrategyFactory accessStrategyFactory, RedisTemplate<String, Object> template, String regionName) {
        this.accessStrategyFactory = accessStrategyFactory;
        this.template = template;
        this.name = regionName;
    }

    public String getName() {
        return name;
    }

    @Override
    public void destroy() throws CacheException {
        template.delete(name);
    }

    /**
     * confirm the specified key exists in current region
     *
     * @param key cache key
     * @return if cache key is exists in current region return true, else return false
     */
    @Override
    public boolean contains(Object key) {
        HashOperations<String, String, Object> hashOperations = template.opsForHash();
        return hashOperations.hasKey(name, key);
    }

    @Override
    public long getSizeInMemory() {
        return -1;
    }

    @Override
    public long getElementCountInMemory() {
        return -1;
    }

    @Override
    public long getElementCountOnDisk() {
        return -1;
    }

    @Override
    public Map toMap() {
        HashOperations<String, String, Object> hashOperations = template.opsForHash();
        return hashOperations.entries(name);
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public int getTimeout() {
        return DEFAULT_CACHE_LOCK_TIMEOUT;
    }

    int getExpireInSeconds() {
        return Math.toIntExact(template.getExpire(name, TimeUnit.SECONDS));
    }

    RedisAccessStrategyFactory getAccessStrategyFactory() {
        return accessStrategyFactory;
    }

    public RedisTemplate<String, Object> getTemplate() {
        return template;
    }
}
