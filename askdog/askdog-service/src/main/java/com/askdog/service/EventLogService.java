package com.askdog.service;

import com.askdog.model.common.EventType;
import com.askdog.model.data.EventLog;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Nonnull;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("service")
@RequestMapping("/service/event_logs")
public interface EventLogService {

    @Nonnull
    @RequestMapping("/{logId}")
    EventLog findById(@PathVariable("logId") String logId);

    @Nonnull
    @RequestMapping(method = POST)
    EventLog save(@RequestBody EventLog eventLog);

    @RequestMapping("last_event")
    EventLog findLatestEvent(@RequestParam("userId") Long userId, @RequestParam("eventType") EventType eventType, @RequestParam("targetId") Long targetId);
}
