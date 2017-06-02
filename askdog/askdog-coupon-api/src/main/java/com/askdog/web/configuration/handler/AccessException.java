package com.askdog.web.configuration.handler;

import com.askdog.common.exception.AbstractException;
import com.askdog.common.exception.Message;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

import static com.askdog.web.configuration.handler.AccessException.Error.*;

public class AccessException extends AbstractException {

    private static final long serialVersionUID = 5147201406297373117L;

    public static final Map<String, Error> codeMap = new HashMap<>();

    public enum Error {
        DENIED,
        AUTH_REQUIRED,
        AUTH_FAILED,
        BAD_CREDENTIALS,
        ACCOUNT_LOCKED,
        CREDENTIALS_EXPIRED,
        ACCOUNT_EXPIRED,
        USER_NOT_FOUND
    }

    static {
        codeMap.put(AccessDeniedException.class.getTypeName(), DENIED);
        codeMap.put(BadCredentialsException.class.getTypeName(), BAD_CREDENTIALS);
        codeMap.put(LockedException.class.getTypeName(), ACCOUNT_LOCKED);
        codeMap.put(CredentialsExpiredException.class.getTypeName(), CREDENTIALS_EXPIRED);
        codeMap.put(AccountExpiredException.class.getTypeName(), ACCOUNT_EXPIRED);
        codeMap.put(UsernameNotFoundException.class.getTypeName(), USER_NOT_FOUND);
        codeMap.put(InsufficientAuthenticationException.class.getTypeName(), AUTH_REQUIRED);
    }

    public AccessException() {
        super(DENIED);
    }

    public AccessException(Message message) {
        super(message);
        setCode(Error.valueOf(message.getPartial()));
    }

    public AccessException(AccessDeniedException exception) {
        super(DENIED, exception);
        setCodeValue(exception.getClass().getTypeName());
    }

    public AccessException(AuthenticationException exception) {
        super(codeMap.getOrDefault(exception.getClass().getTypeName(), AUTH_FAILED), exception);
    }

    @Override
    protected String messageResourceBaseName() {
        return "exception.web";
    }

    @Override
    protected String moduleName() {
        return "WEB_ACCESS";
    }
}
