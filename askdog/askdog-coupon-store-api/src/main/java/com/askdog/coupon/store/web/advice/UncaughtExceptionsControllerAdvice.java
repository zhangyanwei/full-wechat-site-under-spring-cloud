package com.askdog.coupon.store.web.advice;

import com.askdog.common.exception.*;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.ForbiddenException;
import com.askdog.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice(annotations = {RestController.class})
public class UncaughtExceptionsControllerAdvice {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    @ResponseStatus(BAD_REQUEST)
//    public RepresentationMessage handleArgumentErrors(MethodArgumentNotValidException exception) {
//        return new RepresentationMessage(new RequestValidateException(exception));
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseBody
//    @ResponseStatus(BAD_REQUEST)
//    public RepresentationMessage handleArgumentErrors(IllegalArgumentException exception) {
//        return new RepresentationMessage(new RequestValidateException(exception));
//    }
//
//    @ExceptionHandler(IllegalStateException.class)
//    @ResponseBody
//    @ResponseStatus(BAD_REQUEST)
//    public RepresentationMessage handleArgumentErrors(IllegalStateException exception) {
//        return new RepresentationMessage(new BadRequestException(exception));
//    }

//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseBody
//    @ResponseStatus(FORBIDDEN)
//    public RepresentationMessage runtimeException(AccessDeniedException exception) {
//        return new RepresentationMessage(new AccessException(exception));
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseBody
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    public RepresentationMessage runtimeException(RuntimeException exception) {
//        return new RepresentationMessage(new WrappedRuntimeException(exception));
//    }

    @ExceptionHandler(WrappedRuntimeException.class)
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public RepresentationMessage runtimeException(WrappedRuntimeException exception) {
        return new RepresentationMessage(exception);
    }

    @ExceptionHandler(AbstractRuntimeException.class)
    @ResponseBody
    public ResponseEntity<RepresentationMessage> serviceException(AbstractRuntimeException exception) {
        HttpStatus status = BAD_REQUEST;

        if (exception instanceof NotFoundException) {
            status = NOT_FOUND;
        } else if (exception instanceof ConflictException) {
            status = CONFLICT;
        } else if (exception instanceof ForbiddenException) {
            status = FORBIDDEN;
        }

        return ResponseEntity.status(status)
                .contentType(APPLICATION_JSON_UTF8)
                .body(new RepresentationMessage(exception));
    }

    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public RepresentationMessage serviceException(AbstractException exception) {
        return new RepresentationMessage(exception);
    }

    public static class RepresentationMessage implements Serializable {

        private static final long serialVersionUID = -5825177710860167621L;

        private Message message;

        public RepresentationMessage(AdException exception) {
            this.message = new Message(exception);
        }

        public String getCode() {
            return message.getCode();
        }

        public String getMessage() {
            return message.getMessage();
        }

        public String getDetail() {
            return message.getDetail();
        }

        public static RepresentationMessage from(AuthenticationException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof AbstractException) {
                return new RepresentationMessage((AbstractException) cause);
            }

            if (cause instanceof AbstractRuntimeException) {
                return new RepresentationMessage((AbstractRuntimeException) cause);
            }
            //return new RepresentationMessage(new AccessException(exception));
            return null;
        }
    }

}