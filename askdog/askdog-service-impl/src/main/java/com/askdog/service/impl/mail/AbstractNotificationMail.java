package com.askdog.service.impl.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public abstract class AbstractNotificationMail extends AbstractMail {

    @Autowired
    @Qualifier("mc_notification")
    private MailConfiguration mailConfiguration = new MailConfiguration();

    @Override
    MailConfiguration getMailConfiguration() {
        return mailConfiguration;
    }

    @Bean(name = "mc_notification")
    @ConfigurationProperties(prefix = "askdog.mail.notification")
    MailConfiguration mailConfiguration() {
        return new MailConfiguration();
    }

}
