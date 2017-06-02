package com.askdog.service.impl.cache.annotation.coupon;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.coupon.CouponBasicCache.KEY;
import static com.askdog.service.impl.cache.annotation.coupon.CouponBasicCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface CouponBasicCache {
    String KEY = "#couponId";
    String VALUE = "cache:service:coupon:basic";
}
