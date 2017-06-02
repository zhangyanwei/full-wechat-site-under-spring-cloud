package com.askdog.service.bo.system.event;

import com.askdog.model.common.EventType;

import java.io.Serializable;

public class EventRo implements Serializable {

    private static final long serialVersionUID = -4071253088960929758L;

    private EventUser user;
    private EventType type;
    private EventTarget target;

    public EventUser getUser() {
        return user;
    }

    public EventRo setUser(EventUser user) {
        this.user = user;
        return this;
    }

    public EventType getType() {
        return type;
    }

    public EventRo setType(EventType type) {
        this.type = type;
        return this;
    }

    public EventTarget getTarget() {
        return target;
    }

    public EventRo setTarget(EventTarget target) {
        this.target = target;
        return this;
    }
}