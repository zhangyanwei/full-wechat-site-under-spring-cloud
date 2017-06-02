package com.askdog.common.exception;

public class CommonException extends AbstractException {

    public enum Error {
        QR_CODE_GENERATE_FAILED
    }

    public CommonException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public CommonException(Enum<?> code) {
        super(code);
    }

    public CommonException(Enum<?> code, Throwable cause) {
        super(code, cause);
    }

    public CommonException(Error error) {
        super(error);
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.common";
    }

    @Override
    protected String moduleName() {
        return "COM";
    }
}
