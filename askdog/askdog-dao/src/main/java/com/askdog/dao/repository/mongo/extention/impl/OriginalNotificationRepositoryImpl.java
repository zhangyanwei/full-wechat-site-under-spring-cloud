package com.askdog.dao.repository.mongo.extention.impl;

import com.askdog.dao.repository.mongo.extention.OriginalNotificationRepositoryExtention;
import com.askdog.model.data.OriginalNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

public class OriginalNotificationRepositoryImpl implements OriginalNotificationRepositoryExtention {

    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public void readAll(Long userId) {
        mongoTemplate.updateMulti(query(where("recipient").is(userId)), update("read", true), OriginalNotification.class);
    }
}