package com.askdog.messaging.core;

import java.util.Date;

public class DefaultMessage<T> implements Message {

    private static final long serialVersionUID = 2810369653248649490L;

    private T payload;
    private Date sendDate;

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}