package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WxTransInfo {

    @JsonProperty("KfAccount")
    private String kfAccount;

    public String getKfAccount() {
        return kfAccount;
    }

    public void setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
    }
}
