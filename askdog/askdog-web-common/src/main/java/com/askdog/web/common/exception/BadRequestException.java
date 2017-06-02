package com.askdog.web.common.exception;

import com.askdog.common.exception.AbstractRuntimeException;
import com.askdog.common.exception.Message;

import static com.askdog.web.common.exception.BadRequestException.Error.FAILED;

public class BadRequestException extends AbstractRuntimeException {

    private static final long serialVersionUID = 5388350327248338539L;

    public enum Error {
        FAILED,
        NO_EXTERNAL_USER,
        WITHDRAWAL_INTERVAL_TOO_SHORT
    }

    public BadRequestException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public BadRequestException(Error error) {
        super(error);
    }
    public BadRequestException(IllegalStateException exception) {
        super(FAILED, exception.getMessage());
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.web";
    }

    @Override
    protected String moduleName() {
        return "WEB_BAD_REQ";
    }

}