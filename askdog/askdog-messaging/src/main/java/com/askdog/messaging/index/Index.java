package com.askdog.messaging.index;

import com.askdog.model.common.EventType;

import java.io.Serializable;

public abstract class Index implements Serializable {

    private static final long serialVersionUID = 8871028340943458076L;

    private EventType type;
    private Long target;

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

}
