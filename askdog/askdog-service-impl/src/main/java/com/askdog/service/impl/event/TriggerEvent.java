package com.askdog.service.impl.event;

import com.askdog.model.common.EventType;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD})
@Retention(RUNTIME)
@Documented
@Inherited
@Component
public @interface TriggerEvent {
    TriggerEventItem[] value();

    @Retention(RUNTIME)
    @Target({})
    @interface TriggerEventItem {
        String condition() default "true";

        String performer() default "";

        EventType eventType();

        String target() default "";
    }
}