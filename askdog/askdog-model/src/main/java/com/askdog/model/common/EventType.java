package com.askdog.model.common;

import static com.askdog.model.common.EventType.EventTypeAction.*;
import static com.askdog.model.common.EventType.EventTypeGroup.*;

public enum EventType {
    CREATE_USER(CREATE, USER),
    UPDATE_USER(UPDATE, USER),
    CONFIRM_USER(CONFIRM, USER),
    USER_LOGIN(LOGIN, USER),
    UP_VOTE_PRODUCT(UP_VOTE, PRODUCT),
    UN_VOTE_PRODUCT(UN_VOTE, PRODUCT),
    DOWN_VOTE_PRODUCT(DOWN_VOTE, PRODUCT),
    VERIFY_USER_COUPON(VERIFY, USER_COUPON),
    BIND_STORE(BIND, STORE);

    public enum EventTypeAction {
        CREATE, DELETE, UPDATE,
        CONFIRM, LOGIN,
        UP_VOTE, UN_VOTE, DOWN_VOTE,
        VERIFY, BIND
    }

    public enum EventTypeGroup {
        USER, PRODUCT, USER_COUPON, STORE
    }

    private EventTypeAction eventTypeAction;
    private EventTypeGroup eventTypeGroup;

    EventType(EventTypeAction eventTypeAction, EventTypeGroup eventTypeGroup) {
        this.eventTypeAction = eventTypeAction;
        this.eventTypeGroup = eventTypeGroup;
    }

    public EventTypeAction getEventTypeAction() {
        return eventTypeAction;
    }

    public EventTypeGroup getEventTypeGroup() {
        return eventTypeGroup;
    }

}