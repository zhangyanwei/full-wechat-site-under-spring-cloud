package com.askdog.service.impl.cache.annotation.store;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.store.StoreBasicCache.KEY;
import static com.askdog.service.impl.cache.annotation.store.StoreBasicCache.VALUE;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(put = @CachePut(value = VALUE, key = KEY))
public @interface StoreBasicCacheRefresh {
}
