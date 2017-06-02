package com.askdog.web.common.exception;

import com.askdog.common.exception.AbstractRuntimeException;
import com.askdog.common.exception.Message;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

public class ReferenceException extends AbstractRuntimeException {

    private static final long serialVersionUID = -1196230020798256751L;

    public enum Error {
        REFERENCE_FAILED
    }

    public ReferenceException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public ReferenceException(JpaObjectRetrievalFailureException e) {
        super(Error.REFERENCE_FAILED, e);
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.web";
    }

    @Override
    protected String moduleName() {
        return "WEB_VALIDATE";
    }
}
