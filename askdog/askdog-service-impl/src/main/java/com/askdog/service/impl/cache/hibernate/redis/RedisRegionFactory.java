package com.askdog.service.impl.cache.hibernate.redis;

import org.hibernate.cache.CacheException;
import org.hibernate.cfg.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Properties;

public class RedisRegionFactory extends AbstractRedisRegionFactory {

    private static final long serialVersionUID = 474207684119131489L;

    private static Logger logger = LoggerFactory.getLogger(RedisRegionFactory.class);

    private Settings settings;

    @Override
    public synchronized void start(Settings settings, Properties properties) throws CacheException {
        logger.info("starting RedisRegionFactory...");
        this.settings = settings;
    }

    @Override
    public synchronized void stop() {
        logger.debug("stopping RedisRegionFactory...");
    }

    @Override
    protected Settings getSettings() {
        return settings;
    }

    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return HibernateL2CacheTemplate.redisTemplate();
    }
}
