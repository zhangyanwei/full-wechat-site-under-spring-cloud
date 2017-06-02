package com.askdog.service.impl.cache.annotation.store;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.store.StoreBasicCache.KEY;
import static com.askdog.service.impl.cache.annotation.store.StoreBasicCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface StoreBasicCache {
    String VALUE = "cache:service:store:basic";
    String KEY = "#storeId";
}
