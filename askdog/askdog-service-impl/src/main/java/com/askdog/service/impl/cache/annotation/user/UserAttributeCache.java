package com.askdog.service.impl.cache.annotation.user;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.user.UserAttributeCache.KEY;
import static com.askdog.service.impl.cache.annotation.user.UserAttributeCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface UserAttributeCache {
    String VALUE = "cache:service:user:attribute";
    String KEY = "#userId";
}
