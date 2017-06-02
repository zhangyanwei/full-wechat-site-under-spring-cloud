package com.askdog.messaging.redis;

import java.lang.annotation.*;

import static org.springframework.data.redis.listener.adapter.MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MessageListener {
    String[] value() default {};
    String[] topic() default {};
    String[] pattern() default {};
    String listenerMethod() default ORIGINAL_DEFAULT_LISTENER_METHOD;
}
