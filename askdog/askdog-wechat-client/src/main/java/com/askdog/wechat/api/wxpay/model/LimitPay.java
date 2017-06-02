package com.askdog.wechat.api.wxpay.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LimitPay {

    NO_CREDIT("no_credit");

    private String value;

    LimitPay(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
