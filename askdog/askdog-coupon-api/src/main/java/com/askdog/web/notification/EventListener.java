package com.askdog.web.notification;

import com.askdog.messaging.event.Event;
import com.askdog.messaging.event.EventMessage;
import com.askdog.messaging.event.EventMessageInputChannel;
import com.askdog.service.helper.EventHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static com.askdog.messaging.event.EventMessageInputChannel.EVENT;
import static com.askdog.model.common.EventType.EventTypeGroup.STORE;
import static com.askdog.model.common.EventType.VERIFY_USER_COUPON;
import static org.slf4j.LoggerFactory.getLogger;

@EnableBinding(EventMessageInputChannel.class)
public class EventListener {
    private static final Logger logger = getLogger(EventListener.class);

    @Autowired private SimpMessagingTemplate template;
    @Autowired private EventHelper eventHelper;

    @StreamListener(EVENT)
    public void handle(EventMessage eventMessage) {
        Event event = eventMessage.getPayload();

        switch (event.getEventType()) {
            case VERIFY_USER_COUPON:
                template.convertAndSend("/topic/event/coupon/verification", eventHelper.convert(event.getPerformerId(), event.getEventType(), event.getTargetId()));
                break;

            case BIND_STORE:
                template.convertAndSend("/topic/event/store/bind_employee", eventHelper.convert(event.getPerformerId(), event.getEventType(), event.getTargetId()));
                break;
        }

    }
}