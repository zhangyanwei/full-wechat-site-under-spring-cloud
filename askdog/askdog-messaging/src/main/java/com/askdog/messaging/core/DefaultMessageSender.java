package com.askdog.messaging.core;

import org.springframework.messaging.MessageChannel;

import java.util.Date;

import static org.springframework.messaging.support.MessageBuilder.withPayload;

public class DefaultMessageSender<T extends DefaultMessage> implements MessageSender<T> {

    private MessageChannel messageChannel;

    public DefaultMessageSender(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    @Override
    public void send(T message) {
        message.setSendDate(new Date());
        messageChannel.send(withPayload(message).build());
    }
}