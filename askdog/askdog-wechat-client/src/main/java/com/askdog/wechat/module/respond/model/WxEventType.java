package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxEventType {

    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe"),
    VIEW("VIEW"),
    CLICK("CLICK");

    private String event;

    WxEventType(String event) {
        this.event = event;
    }

    @JsonValue
    public String getEvent() {
        return event;
    }

    @JsonCreator
    public static WxEventType fromValue(String value) {
        for (WxEventType eventType : values()) {
            if (eventType.event.equals(value)) {
                return eventType;
            }
        }

        throw new IllegalArgumentException("not found WxEventType enum value for " + value);
    }
}
