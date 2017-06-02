package com.askdog.messaging.review;

import com.askdog.model.common.EventType;
import com.askdog.model.common.State;

public class StateChange {

    private Long targetId;
    private EventType.EventTypeGroup resourceType;
    private State state;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public EventType.EventTypeGroup getResourceType() {
        return resourceType;
    }

    public void setResourceType(EventType.EventTypeGroup resourceType) {
        this.resourceType = resourceType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
