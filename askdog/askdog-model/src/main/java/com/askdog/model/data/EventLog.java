package com.askdog.model.data;

import com.askdog.model.common.EventType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "mc_event_log")
public class EventLog extends Base {

    private static final long serialVersionUID = -899941144663519051L;

    private Long performerId;
    private EventType eventType;
    private Long targetId;
    private Date eventDate;

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