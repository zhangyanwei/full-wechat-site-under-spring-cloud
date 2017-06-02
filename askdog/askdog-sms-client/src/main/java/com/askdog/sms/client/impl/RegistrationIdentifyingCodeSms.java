package com.askdog.sms.client.impl;

import com.askdog.sms.client.SmsRequest;
import com.askdog.sms.service.SmsSender;
import org.springframework.stereotype.Component;

// TODO do not put this code here, just move it into the related module.
@Component
public class RegistrationIdentifyingCodeSms extends SmsSender {
    public void send(String phoneNum, String identifyingCode) {
        SmsRequest smsRequest = new SmsRequest();
        String[] phone = {phoneNum};
        String templateId = getSmsTemplateId("RegistrationIdentifyingCode");
        String[] parameters = {identifyingCode};
        smsRequest.setPhoneNumbers(phone);
        smsRequest.setTemplateId(templateId);
        smsRequest.setParameters(parameters);
        send(smsRequest);
    }
}
