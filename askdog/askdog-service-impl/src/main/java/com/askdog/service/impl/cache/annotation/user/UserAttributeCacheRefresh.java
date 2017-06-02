package com.askdog.service.impl.cache.annotation.user;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.user.UserAttributeCache.KEY;
import static com.askdog.service.impl.cache.annotation.user.UserAttributeCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(put = @CachePut(value = VALUE, key = KEY))
public @interface UserAttributeCacheRefresh {
}
