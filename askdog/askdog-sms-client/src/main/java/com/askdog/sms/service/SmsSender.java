package com.askdog.sms.service;

import com.askdog.sms.client.SmsClient;
import com.askdog.sms.client.SmsRequest;
import com.askdog.sms.client.impl.SmsTemplateInfo;
import com.askdog.sms.client.impl.SmsTemplateInfoRepository;
import com.askdog.sms.exception.SmsRuntimeException;
import com.askdog.web.common.async.AsyncCaller;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SmsSender implements Sms {

    @Autowired
    private SmsClient smsClient;
    @Autowired
    private AsyncCaller asyncCaller;
    @Autowired
    private SmsTemplateInfoRepository smsTemplateInfoRepository;

    @Override
    public void send(SmsRequest smsRequest) {
        asyncCaller.asyncCall(() -> {
            try {
                smsClient.sendSmsMessage(smsRequest);
            } catch (SmsRuntimeException e) {
                e.printStackTrace();
            }
        });
    }

    public String getSmsTemplateId(String smsTemplateName) {
        SmsTemplateInfo smsTemplateInfo = smsTemplateInfoRepository.findByName(smsTemplateName).orElse(null);
        if (null != smsTemplateInfo) {
            return smsTemplateInfo.getTemplateID();
        }
        return null;
    }
}
