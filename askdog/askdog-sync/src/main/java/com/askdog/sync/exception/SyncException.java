package com.askdog.sync.exception;

import com.askdog.common.exception.AbstractException;

public class SyncException extends RuntimeException {

    private static final long serialVersionUID = 2718644240141775780L;

    public SyncException(AbstractException exception) {
        super(exception);
    }

}
