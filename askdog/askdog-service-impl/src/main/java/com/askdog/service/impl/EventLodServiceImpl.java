package com.askdog.service.impl;

import com.askdog.dao.repository.mongo.EventLogRepository;
import com.askdog.model.common.EventType;
import com.askdog.model.data.EventLog;
import com.askdog.service.EventLogService;
import com.askdog.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;

import static com.askdog.service.exception.NotFoundException.Error.EVENT_LOG;

@RestController
public class EventLodServiceImpl implements EventLogService {

    @Autowired
    private EventLogRepository eventLogRepository;

    @Nonnull
    @Override
    public EventLog findById(@PathVariable("logId") String logId) {
        return eventLogRepository.findById(logId).orElseThrow(() -> new NotFoundException(EVENT_LOG));
    }

    @Nonnull
    @Override
    public EventLog save(@RequestBody EventLog eventLog) {
        return eventLogRepository.save(eventLog);
    }

    @Nonnull
    @Override
    public EventLog findLatestEvent(@RequestParam("userId") Long userId, @RequestParam("eventType") EventType eventType, @RequestParam("targetId") Long targetId) {
        return eventLogRepository.findFirstByPerformerIdAndEventTypeAndTargetIdOrderByEventDateDesc(userId, eventType, targetId);
    }
}