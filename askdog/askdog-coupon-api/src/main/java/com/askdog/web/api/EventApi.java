package com.askdog.web.api;

import com.askdog.service.EventService;
import com.askdog.service.bo.event.EventDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/events")
public class EventApi {

    @Autowired
    private EventService eventService;

    @RequestMapping(path = "/{eventId}", method = GET)
    public EventDetail detail(@PathVariable("eventId") Long eventId) {
        return eventService.getEvent(eventId);
    }

}
