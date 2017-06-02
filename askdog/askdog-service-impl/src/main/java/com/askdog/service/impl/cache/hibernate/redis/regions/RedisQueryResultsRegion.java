package com.askdog.service.impl.cache.hibernate.redis.regions;

import com.askdog.service.impl.cache.hibernate.redis.strategy.RedisAccessStrategyFactory;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisQueryResultsRegion extends RedisGeneralDataRegion implements QueryResultsRegion {

    public RedisQueryResultsRegion(RedisAccessStrategyFactory accessStrategyFactory,
                                   RedisTemplate<String, Object> template,
                                   String regionName) {
        super(accessStrategyFactory, template, regionName);
    }
}
