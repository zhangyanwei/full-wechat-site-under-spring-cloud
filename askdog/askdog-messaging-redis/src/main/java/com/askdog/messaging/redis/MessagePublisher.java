package com.askdog.messaging.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class MessagePublisher {

    @Autowired
    @Qualifier("message.listeners")
    private RedisTemplate redisTemplate;

    public void publish(String topic, Serializable value) {
        redisTemplate.convertAndSend(topic, value);
    }

}
