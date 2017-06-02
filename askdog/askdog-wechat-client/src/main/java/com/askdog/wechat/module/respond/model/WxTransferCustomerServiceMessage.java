package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("transfer_customer_service")
public class WxTransferCustomerServiceMessage extends WxMessage {

    @JsonProperty("TransInfo")
    private WxTransInfo transInfo;

    public WxTransInfo getTransInfo() {
        return transInfo;
    }

    public void setTransInfo(WxTransInfo transInfo) {
        this.transInfo = transInfo;
    }
}
