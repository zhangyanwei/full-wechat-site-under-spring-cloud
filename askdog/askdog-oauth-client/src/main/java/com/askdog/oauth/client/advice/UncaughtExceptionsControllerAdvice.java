//package com.askdog.oauth.client.advice;
//
//import com.askdog.common.exception.*;
//import com.askdog.service.exception.ConflictException;
//import com.askdog.service.exception.ForbiddenException;
//import com.askdog.service.exception.NotFoundException;
//import com.askdog.web.common.exception.BadRequestException;
//import com.askdog.web.common.exception.RequestValidateException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.*;
//
//import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8;
//import static org.springframework.http.HttpStatus.*;
//
//@ControllerAdvice(annotations = {RestController.class})
//public class UncaughtExceptionsControllerAdvice {
//
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
//
////    @ExceptionHandler(RuntimeException.class)
////    @ResponseBody
////    @ResponseStatus(INTERNAL_SERVER_ERROR)
////    public RepresentationMessage runtimeException(RuntimeException exception) {
////        return new RepresentationMessage(new WrappedRuntimeException(exception));
////    }
//
//    @ExceptionHandler(WrappedRuntimeException.class)
//    @ResponseBody
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    public RepresentationMessage runtimeException(WrappedRuntimeException exception) {
//        return new RepresentationMessage(exception);
//    }
//
//    @ExceptionHandler(AbstractRuntimeException.class)
//    @ResponseBody
//    public ResponseEntity<RepresentationMessage> serviceException(AbstractRuntimeException exception) {
//        HttpStatus status = BAD_REQUEST;
//
//        if (exception instanceof NotFoundException) {
//            status = NOT_FOUND;
//
//        } else if (exception instanceof ForbiddenException) {
//            status = FORBIDDEN;
//
//        } else if (exception instanceof ConflictException) {
//            status = CONFLICT;
//        }
//
//        return ResponseEntity.status(status)
//                .contentType(APPLICATION_JSON_UTF8)
//                .body(new RepresentationMessage(exception));
//    }
//
//    @ExceptionHandler(AbstractException.class)
//    @ResponseBody
//    @ResponseStatus(BAD_REQUEST)
//    public RepresentationMessage serviceException(AbstractException exception) {
//        return new RepresentationMessage(exception);
//    }
//
//    public static class RepresentationMessage {
//
//        private Message message;
//
//        public RepresentationMessage(AdException exception) {
//            this.message = new Message(exception);
//        }
//
//        public String getCode() {
//            return message.getCode();
//        }
//
//        public String getMessage() {
//            return message.getMessage();
//        }
//
//        public String getDetail() {
//            return message.getDetail();
//        }
//    }
//
//}