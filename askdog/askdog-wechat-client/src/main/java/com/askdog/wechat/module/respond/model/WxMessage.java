package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Date;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "MsgType",
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WxTextMessage.class),
        @JsonSubTypes.Type(value = WxEventMessage.class),
        @JsonSubTypes.Type(value = WxTransferCustomerServiceMessage.class)
})
@JsonRootName("xml")
public abstract class WxMessage {

    @JsonProperty("ToUserName")
    private String toUserName;

    @JsonProperty("FromUserName")
    private String fromUserName;

    @JsonProperty("CreateTime")
    private Date createTime;

    @JsonProperty("MsgType")
    private WxMessageType messageType;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public WxMessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(WxMessageType messageType) {
        this.messageType = messageType;
    }
}
