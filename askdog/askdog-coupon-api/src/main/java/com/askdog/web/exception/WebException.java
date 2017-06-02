package com.askdog.web.exception;

import com.askdog.common.exception.AbstractException;

import static com.askdog.web.exception.WebException.Error.RUNTIME_ERROR;

public class WebException extends AbstractException {

    private static final long serialVersionUID = -460735736684833105L;

    public enum Error {
        QR_CODE_GENERATE_FAILED,
        RUNTIME_ERROR
    }

    public WebException(Error error, String message) {
        super(error, message);
    }

    public WebException(Error error, Throwable cause) {
        super(error, cause);
    }

    public WebException(Throwable cause) {
        super(RUNTIME_ERROR, cause);
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.web";
    }

    @Override
    protected String moduleName() {
        return "WEB";
    }
}
