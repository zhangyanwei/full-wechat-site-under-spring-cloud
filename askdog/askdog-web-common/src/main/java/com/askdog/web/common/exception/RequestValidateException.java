package com.askdog.web.common.exception;

import com.askdog.common.exception.AbstractRuntimeException;
import com.askdog.common.exception.Message;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Iterator;

import static com.askdog.web.common.exception.RequestValidateException.Error.FAILED;

public class RequestValidateException extends AbstractRuntimeException {

    private static final long serialVersionUID = 5388350327248338539L;

    private String detailMessage;

    public enum Error {
        FAILED
    }

    public RequestValidateException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
        this.detailMessage = message.getMessage();
    }

    public RequestValidateException(MethodArgumentNotValidException exception) {
        setCode(FAILED);
        BindingResult bindingResult = exception.getBindingResult();
        FieldError firstFieldError = bindingResult.getFieldError();
        if (firstFieldError != null) {
            updateCodeMessage(firstFieldError);
        } else {
            Iterator<ObjectError> errors = bindingResult.getAllErrors().iterator();
            if (errors.hasNext()) {
                ObjectError objectError = errors.next();
                updateCodeMessage(objectError);
            }
        }
    }

    public RequestValidateException(IllegalArgumentException exception) {
        super(FAILED, exception);
    }

    @Override
    public String getMessage() {
        return detailMessage;
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.web";
    }

    @Override
    protected String moduleName() {
        return "WEB_VALIDATE";
    }

    private void updateCodeMessage(ObjectError objectError) {
        String[] codes = objectError.getCodes();
        if (codes != null && codes.length > 0) {
            this.detailMessage = codes[0] + ": " + objectError.getDefaultMessage();
        }
    }
}