package com.askdog.sms.service;

import com.askdog.sms.client.SmsRequest;
import com.askdog.sms.exception.SmsRuntimeException;


public interface Sms {
    void send(SmsRequest templateMessage)throws SmsRuntimeException;
}
