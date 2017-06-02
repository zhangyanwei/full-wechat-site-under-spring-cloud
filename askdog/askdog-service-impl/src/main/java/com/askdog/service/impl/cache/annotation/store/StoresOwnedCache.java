package com.askdog.service.impl.cache.annotation.store;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.store.StoresOwnedCache.KEY;
import static com.askdog.service.impl.cache.annotation.store.StoresOwnedCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface StoresOwnedCache {
    String VALUE = "cache:service:stores:owned";
    String KEY = "#userId";
}
