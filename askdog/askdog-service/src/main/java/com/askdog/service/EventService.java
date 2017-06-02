package com.askdog.service;

import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.event.AmendedEvent;
import com.askdog.service.bo.event.EventDetail;
import com.askdog.service.bo.event.PureEvent;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient("service")
@RequestMapping("/service/event")
public interface EventService {

    @RequestMapping(method = POST)
    void createEvent(@RequestParam("userId") Long userId, @RequestBody PureEvent pureEvent);

    @RequestMapping(method = PUT)
    void updateEvent(@RequestParam("userId") Long userId, @RequestBody AmendedEvent amendedEvent);

    @RequestMapping(path = "/{eventId}", method = GET)
    EventDetail getEvent(@PathVariable("eventId") long eventId);

    @RequestMapping(path = "/{eventId}", method = DELETE)
    void deleteEvent(@RequestParam("userId") Long userId, @PathVariable("eventId") long eventId);

    @RequestMapping(path = "/search", method = POST)
    PagedData<EventDetail> getEvents(@RequestParam("userId") Long userId, @RequestParam("storeId") long storeId, @RequestBody Pageable pageable);

}
