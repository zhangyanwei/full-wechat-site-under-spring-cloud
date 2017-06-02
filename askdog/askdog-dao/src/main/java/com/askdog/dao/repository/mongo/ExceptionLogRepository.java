package com.askdog.dao.repository.mongo;

import com.askdog.model.data.ExceptionLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExceptionLogRepository extends MongoRepository<ExceptionLog, String> {
}
