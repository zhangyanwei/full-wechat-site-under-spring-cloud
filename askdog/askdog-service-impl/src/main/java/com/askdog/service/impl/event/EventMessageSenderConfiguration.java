package com.askdog.service.impl.event;

import com.askdog.messaging.event.EventMessageOutputChannel;
import com.askdog.messaging.event.EventMessageSender;
import com.askdog.messaging.notification.NotificationMessageOutputChannel;
import com.askdog.messaging.notification.NotificationMessageSender;
import com.askdog.messaging.review.ReviewMessageOutputChannel;
import com.askdog.messaging.review.ReviewMessageSender;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

@EnableBinding({
        EventMessageOutputChannel.class,
        NotificationMessageOutputChannel.class,
        ReviewMessageOutputChannel.class
})
public class EventMessageSenderConfiguration {
    @Bean
    public EventMessageSender eventMessageSender(EventMessageOutputChannel eventMessageOutputChannel) {
        return new EventMessageSender(eventMessageOutputChannel.output());
    }

    @Bean
    public NotificationMessageSender notificationMessageSender(NotificationMessageOutputChannel notificationMessageOutputChannel) {
        return new NotificationMessageSender(notificationMessageOutputChannel.output());
    }

    @Bean
    public ReviewMessageSender reviewMessageSender(ReviewMessageOutputChannel reviewMessageOutputChannel) {
        return new ReviewMessageSender(reviewMessageOutputChannel.output());
    }
}