package com.askdog.messaging.review;

import com.askdog.messaging.core.DefaultMessageSender;
import org.springframework.messaging.MessageChannel;

public class ReviewMessageSender extends DefaultMessageSender<ReviewMessage> {
    public ReviewMessageSender(MessageChannel messageChannel) {
        super(messageChannel);
    }
}