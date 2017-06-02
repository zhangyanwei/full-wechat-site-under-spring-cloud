package com.askdog.service.impl.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "askdog.cache")
public class CacheConfigParam {

    private  Map<String, Object> expires;
    private Long defaultExpireTime;

    public Long getDefaultExpireTime() {
        return defaultExpireTime;
    }

    public void setDefaultExpireTime(Long defaultExpireTime) {
        this.defaultExpireTime = defaultExpireTime;
    }

    public Map<String, Object> getExpires() {
        return expires;
    }

    public void setExpires(Map<String, Object> expires) {
        this.expires = expires;
    }
}
