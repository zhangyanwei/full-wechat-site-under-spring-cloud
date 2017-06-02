package com.askdog.dao.repository.mongo;

import com.askdog.dao.repository.mongo.extention.ExtendedEventLogRepository;
import com.askdog.model.common.EventType;
import com.askdog.model.data.EventLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventLogRepository extends MongoRepository<EventLog, String>, ExtendedEventLogRepository {
    Optional<EventLog> findById(String id);
    EventLog findFirstByPerformerIdAndEventTypeAndTargetIdOrderByEventDateDesc(Long userId, EventType eventType, Long targetId);
}

