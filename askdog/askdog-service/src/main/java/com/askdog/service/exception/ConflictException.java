package com.askdog.service.exception;

import com.askdog.common.exception.Message;

public class ConflictException extends ServiceRuntimeException {

    private static final long serialVersionUID = 3837761502401319670L;

    public enum Error {
        USER_USERNAME,
        USER_MAIL,
        USER_PHONE,
        NOTIFICATION,
        USER_COUPON,
        VOTE,
        ADD_EMPLOYEE
    }

    public ConflictException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public ConflictException(Error error) {
        super(error);
    }

    @Override
    protected String reasonCode() {
        return "CONFLICT";
    }
}
