package com.askdog.service.impl.cache.annotation.product;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.product.ProductBasicCache.KEY;
import static com.askdog.service.impl.cache.annotation.product.ProductBasicCache.VALUE;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface ProductBasicCache {
    String VALUE = "cache:service:product:basic";
    String KEY = "#productId";
}
