package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WxMessageType {

    TEXT("text"),
    EVENT("event"),
    TRANSFER_CUSTOMER_SERVICE("transfer_customer_service");

    private String type;

    WxMessageType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static WxMessageType fromValue(String value) {
        for (WxMessageType messageType : values()) {
            if (messageType.type.equals(value)) {
                return messageType;
            }
        }

        throw new IllegalArgumentException("not found WxMessageType enum value for " + value);
    }

}
