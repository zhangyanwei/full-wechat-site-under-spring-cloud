package com.askdog.wechat.module.respond.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("event")
public class WxEventMessage extends WxMessage {

    @JsonProperty("Event")
    private WxEventType event;

    @JsonProperty("EventKey")
    private String eventKey;

    public WxEventType getEvent() {
        return event;
    }

    public void setEvent(WxEventType event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
