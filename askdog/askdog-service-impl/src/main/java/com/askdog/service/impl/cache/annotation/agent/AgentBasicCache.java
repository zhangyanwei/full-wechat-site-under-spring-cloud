package com.askdog.service.impl.cache.annotation.agent;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.agent.AgentBasicCache.KEY;
import static com.askdog.service.impl.cache.annotation.agent.AgentBasicCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface AgentBasicCache {
    String VALUE = "cache:service:agent:basic";
    String KEY = "#agentId";
}
