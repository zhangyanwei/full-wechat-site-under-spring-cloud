package com.askdog.coupon.store.web;

import com.askdog.oauth.client.vo.UserInfo;
import com.askdog.service.EventService;
import com.askdog.service.bo.common.PagedData;
import com.askdog.service.bo.event.AmendedEvent;
import com.askdog.service.bo.event.EventDetail;
import com.askdog.service.bo.event.PureEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(value = "/api/events")
public class EventApi {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(method = POST)
    public void create(@AuthenticationPrincipal UserInfo user, @RequestBody PureEvent event) {
        eventService.createEvent(user.getId(), event);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(method = PUT)
    public void update(@AuthenticationPrincipal UserInfo user, @RequestBody AmendedEvent event) {
        eventService.updateEvent(user.getId(), event);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(path = "/{eventId}", method = DELETE)
    public void delete(@AuthenticationPrincipal UserInfo user, @PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(user.getId(), eventId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT_ADMIN', 'ROLE_AGENT_EMPLOYEE', 'ROLE_STORE_ADMIN', 'ROLE_STORE_EMPLOYEE')")
    @RequestMapping(path = "/search", method = GET)
    public PagedData<EventDetail> search(@AuthenticationPrincipal UserInfo user,
                                         @RequestParam("storeId") Long storeId,
                                         @PageableDefault(sort = "creationTime", direction = DESC) Pageable pageable) {
        return eventService.getEvents(user.getId(), storeId, pageable);
    }

}
