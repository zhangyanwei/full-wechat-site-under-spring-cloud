package com.askdog.service.impl.advice;

import com.askdog.common.exception.AbstractException;
import com.askdog.common.exception.AbstractRuntimeException;
import com.askdog.common.exception.Message;
import com.askdog.common.exception.WrappedRuntimeException;
import com.askdog.model.data.ExceptionLog;
import com.askdog.service.ExceptionLogService;
import com.askdog.service.exception.ConflictException;
import com.askdog.service.exception.NotFoundException;
import com.askdog.web.common.exception.BadRequestException;
import com.askdog.web.common.exception.ReferenceException;
import com.askdog.web.common.exception.RequestValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import static com.askdog.service.utils.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice(annotations = {RestController.class})
public class UncaughtExceptionsControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(UncaughtExceptionsControllerAdvice.class);

    @Autowired
    private ExceptionLogService exceptionLogService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message handleArgumentErrors(MethodArgumentNotValidException exception) {
        saveExceptionLog(exception);
        return new Message(new RequestValidateException(exception));
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message handleArgumentErrors(JpaObjectRetrievalFailureException exception) {
        saveExceptionLog(exception);
        return new Message(new ReferenceException(exception));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message handleArgumentErrors(IllegalArgumentException exception) {
        saveExceptionLog(exception);
        return new Message(new RequestValidateException(exception));
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message handleArgumentErrors(IllegalStateException exception) {
        saveExceptionLog(exception);
        return new Message(new BadRequestException(exception));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Message runtimeException(RuntimeException exception) {
        exception.printStackTrace();
        saveExceptionLog(exception);
        return new Message(new WrappedRuntimeException(exception));
    }

    @ExceptionHandler(AbstractRuntimeException.class)
    @ResponseBody
    public ResponseEntity<Message> serviceException(AbstractRuntimeException exception) {

        HttpStatus status = BAD_REQUEST;
        if (exception instanceof NotFoundException) {
            status = NOT_FOUND;
        } else if (exception instanceof ConflictException) {
            status = CONFLICT;
        }
        saveExceptionLog(exception);

        return ResponseEntity.status(status)
                .contentType(APPLICATION_JSON_UTF8)
                .body(new Message(exception));
    }

    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    public Message serviceException(AbstractException exception) {
        saveExceptionLog(exception);
        return new Message(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public void otherException(Exception exception) {
        saveExceptionLog(exception);
    }

    private void saveExceptionLog(Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        StackTraceElement[] etcs = exception.getStackTrace();
        for (StackTraceElement ect : etcs) {
            stringBuilder.append(ect.toString()).append("___");
        }
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setOccurenceTime(new Date());
        exceptionLog.setMachineId(System.getProperties().get("machine.id").toString());
        exceptionLog.setMessage(exception.getMessage());
        exceptionLog.setStackInfo(stringBuilder.toString());
        exceptionLogService.save(exceptionLog);
    }
}