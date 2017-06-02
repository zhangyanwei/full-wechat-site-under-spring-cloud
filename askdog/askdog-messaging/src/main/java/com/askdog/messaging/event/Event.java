package com.askdog.messaging.event;

import com.askdog.model.common.EventType;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

    private static final long serialVersionUID = 183632122849909776L;

    private Long performerId;
    private EventType eventType;
    private Long targetId;
    private Date eventDate = new Date();

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}