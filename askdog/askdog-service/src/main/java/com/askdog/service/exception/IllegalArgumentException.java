package com.askdog.service.exception;

import com.askdog.common.exception.Message;

public class IllegalArgumentException extends ServiceRuntimeException {

    private static final long serialVersionUID = -6299563608663320053L;

    public enum Error {
        INVALID_TOKEN,
        INVALID_ORIGIN_PASSWORD,
        INVALID_IDENTIFYING_CODE,
        INVALID_RESOURCE,
        INVALID_UNIT,
        INVALID_FORWARD_COUPON,
        INVALID_AD_CODE,
        INVALID_GEO
    }

    public IllegalArgumentException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public IllegalArgumentException(Error error) {
        super(error);
    }

    @Override
    protected String reasonCode() {
        return "ARG";
    }

}
