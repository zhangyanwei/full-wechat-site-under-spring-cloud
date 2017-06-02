package com.askdog.model.redis;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public abstract class AbstractRedisTemplate<K, V> extends RedisTemplate<K, V> {

    public AbstractRedisTemplate(RedisConnectionFactory connectionFactory) {
        initDefaultKeySerializers();
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new DefaultStringRedisConnection(connection);
    }

    abstract void initDefaultKeySerializers();

}
