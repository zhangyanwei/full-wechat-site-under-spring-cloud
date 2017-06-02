package com.askdog.message.process;

import com.askdog.messaging.event.Event;
import com.askdog.messaging.event.EventMessage;
import com.askdog.messaging.event.EventMessageInputChannel;
import com.askdog.model.common.EventType.EventTypeGroup;
import com.askdog.service.MessageService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import static com.askdog.messaging.event.EventMessageInputChannel.EVENT;
import static org.slf4j.LoggerFactory.getLogger;

@EnableBinding(EventMessageInputChannel.class)
@SuppressWarnings("SpringFacetCodeInspection")
public class EventToIndexMessageProcessor {
    private static final Logger logger = getLogger(EventToIndexMessageProcessor.class);

    @Autowired private MessageService messageService;

    @StreamListener(EVENT)
    public void process(EventMessage eventMessage) {
        Event event = eventMessage.getPayload();
        logger.info("Process event: type: {}, target: {}", event.getEventType(), event.getTargetId());

        EventTypeGroup eventTypeGroup = event.getEventType().getEventTypeGroup();
        // TODO hand the event to determine whether update the indices
    }
}