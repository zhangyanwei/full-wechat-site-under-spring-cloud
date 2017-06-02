package com.askdog.service.impl.cache.hibernate.redis;

import org.springframework.data.redis.core.RedisTemplate;

public class HibernateL2CacheTemplate {

    private static final HibernateL2CacheTemplate INSTANCE = new HibernateL2CacheTemplate();

    private RedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    static RedisTemplate<String, Object> redisTemplate() {
        return INSTANCE.redisTemplate;
    }

    public static void redisTemplate(RedisTemplate redisTemplate) {
        INSTANCE.redisTemplate = redisTemplate;
    }

}
