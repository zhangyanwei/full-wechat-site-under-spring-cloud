package com.askdog.service.exception;

import com.askdog.common.exception.Message;

public class NotFoundException extends ServiceRuntimeException {

    private static final long serialVersionUID = -2909833311527921881L;

    public enum Error {
        USER,
        PRODUCT,
        EVENT,
        NOTIFICATION,
        STORAGE_RECORD,
        USER_RESIDENCE,
        EVENT_LOG,
        REVIEW_ITEM,
        AGENT,
        STORE,
        COUPON,
        USER_COUPON,
        EMPLOYEE,
        TOKEN
    }

    public NotFoundException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public NotFoundException(Error error) {
        super(error);
    }

    @Override
    protected String reasonCode() {
        return "NOT_FOUND";
    }
}
