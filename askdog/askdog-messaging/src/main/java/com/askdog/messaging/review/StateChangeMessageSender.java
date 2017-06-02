package com.askdog.messaging.review;

import com.askdog.messaging.core.DefaultMessageSender;
import org.springframework.messaging.MessageChannel;

public class StateChangeMessageSender extends DefaultMessageSender<StateChangeMessage> {
    public StateChangeMessageSender(MessageChannel messageChannel) {
        super(messageChannel);
    }
}