package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("text")
public class WxTextMessage extends WxMessage {

    @JsonProperty("Content")
    private String content;

    @JsonProperty("MsgId")
    private String messageId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
