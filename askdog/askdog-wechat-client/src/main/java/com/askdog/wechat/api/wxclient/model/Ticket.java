package com.askdog.wechat.api.wxclient.model;

import com.askdog.wechat.api.WxObject;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket implements WxObject {

    @JsonProperty("errcode")
    private String errorCode;
    @JsonProperty("errmsg")
    private String errorMessage;
    private String ticket;
    private int expiresIn;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
