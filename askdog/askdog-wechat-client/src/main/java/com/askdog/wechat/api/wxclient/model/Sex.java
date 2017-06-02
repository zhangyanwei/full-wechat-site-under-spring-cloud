package com.askdog.wechat.api.wxclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sex {

    MALE(1), FEMALE(2), UNKNOWN(0);

    private int value;

    Sex(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static Sex fromValue(int value) {
        for (Sex sex : values()) {
            if (sex.value == value) {
                return sex;
            }
        }

        throw new IllegalArgumentException("not found Sex enum value for " + value);
    }
}
