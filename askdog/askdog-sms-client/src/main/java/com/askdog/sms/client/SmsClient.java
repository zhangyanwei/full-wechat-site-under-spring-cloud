package com.askdog.sms.client;

import com.askdog.sms.exception.SmsRuntimeException;

import java.util.List;

public interface SmsClient {
    void sendSmsMessage(SmsRequest templateMessage) throws SmsRuntimeException;
    List<? extends SmsTemplate> findSmsTemplates(String templateId) throws SmsRuntimeException;
}
