package com.askdog.sms.client.impl;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SmsTemplateInfoRepository extends MongoRepository<SmsTemplateInfo, String> {
    Optional<SmsTemplateInfo> findByName(String name);
}
