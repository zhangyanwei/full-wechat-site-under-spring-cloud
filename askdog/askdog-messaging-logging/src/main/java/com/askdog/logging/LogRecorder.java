package com.askdog.logging;

import com.askdog.messaging.event.Event;
import com.askdog.messaging.event.EventMessage;
import com.askdog.messaging.event.EventMessageInputChannel;
import com.askdog.model.common.EventType;
import com.askdog.model.data.EventLog;
import com.askdog.model.data.inner.NotificationType;
import com.askdog.service.EventLogService;
import com.askdog.service.NotificationService;
import com.askdog.service.bo.PureNotification;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.EnumSet;

import static com.askdog.messaging.event.EventMessageInputChannel.EVENT;
import static org.slf4j.LoggerFactory.getLogger;

@EnableBinding(EventMessageInputChannel.class)
public class LogRecorder {

    private static final Logger logger = getLogger(LogRecorder.class);

    @Autowired private EventLogService eventLogService;
    @Autowired private NotificationService notificationService;

    @StreamListener(EVENT)
    public void handle(EventMessage eventMessage) {
        Event event = eventMessage.getPayload();
        logger.info("[Logging] received the event: type: {}, target: {}", event.getEventType(), event.getTargetId());
        EventLog eventLog = new EventLog();
        eventLog.setPerformerId(event.getPerformerId());
        eventLog.setEventType(event.getEventType());
        eventLog.setTargetId(event.getTargetId());
        eventLog.setEventDate(event.getEventDate());
        EventLog savedEventLog = eventLogService.save(eventLog);

        // TODO define the noticeable events.
        EnumSet<EventType> noticeableEvents = EnumSet.noneOf(EventType.class);
        if (noticeableEvents.contains(event.getEventType())) {
            PureNotification pureNotification = new PureNotification();
            pureNotification.setNotificationType(NotificationType.EVENT);
            pureNotification.setLogId(savedEventLog.getId());
            notificationService.save(pureNotification);
        }
    }
}