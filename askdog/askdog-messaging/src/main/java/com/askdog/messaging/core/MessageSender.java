package com.askdog.messaging.core;

public interface MessageSender<T extends Message> {
    void send(T message);
}