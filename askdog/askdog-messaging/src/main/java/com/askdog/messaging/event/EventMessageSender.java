package com.askdog.messaging.event;

import com.askdog.messaging.core.DefaultMessageSender;
import org.springframework.messaging.MessageChannel;

public class EventMessageSender extends DefaultMessageSender<EventMessage> {
    public EventMessageSender(MessageChannel messageChannel) {
        super(messageChannel);
    }
}