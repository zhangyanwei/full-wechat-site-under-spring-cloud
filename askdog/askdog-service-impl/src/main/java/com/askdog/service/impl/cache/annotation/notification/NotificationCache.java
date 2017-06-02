package com.askdog.service.impl.cache.annotation.notification;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.askdog.service.impl.cache.annotation.notification.NotificationCache.KEY;
import static com.askdog.service.impl.cache.annotation.notification.NotificationCache.VALUE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(cacheable = @Cacheable(value = VALUE, key = KEY))
public @interface NotificationCache {
    String VALUE = "cache:service:notification";
    String KEY = "#originalNotification.id";
}
