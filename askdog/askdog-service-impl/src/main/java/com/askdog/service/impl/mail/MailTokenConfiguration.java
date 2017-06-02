package com.askdog.service.impl.mail;

import java.util.concurrent.TimeUnit;

public class MailTokenConfiguration extends MailConfiguration {

    private int tokenTimeout;
    private TimeUnit tokenTimeoutUnit;

    public int getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(int tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public TimeUnit getTokenTimeoutUnit() {
        return tokenTimeoutUnit;
    }

    public void setTokenTimeoutUnit(TimeUnit tokenTimeoutUnit) {
        this.tokenTimeoutUnit = tokenTimeoutUnit;
    }

}
