package com.askdog.wechat.api.wxclient.model;

import com.askdog.wechat.api.WxObject;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WxResult implements WxObject {

    @JsonProperty(value = "errcode")
    private int code;

    @JsonProperty(value = "errmsg")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
