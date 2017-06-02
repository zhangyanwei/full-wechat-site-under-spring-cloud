package com.askdog.service;

import com.askdog.model.business.Notification;
import com.askdog.model.common.EventType;
import com.askdog.model.data.ReviewItem;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("service")
@RequestMapping("/service/message/")
public interface MessageService {

    @RequestMapping(value = "/event", method = POST)
    void sendEventMessage(@RequestParam(value = "performerId", required = false) Long performerId,
                          @RequestParam(value = "eventType", required = false) EventType eventType,
                          @RequestParam(value = "targetId", required = false) Long targetId);

    @RequestMapping(value = "/notification", method = POST)
    void sendNotificationMessage(@RequestBody Notification notification);

    @RequestMapping(value = "/review", method = POST)
    void sendReviewMessage(@RequestBody ReviewItem reviewItem);

}
