package com.askdog.service.impl.mail;

import com.askdog.common.FreemarkerTemplate;
import com.askdog.common.TemplateSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RegistrationTokenMail extends AbstractTokenMail {

    @TemplateSource(name = "registration-confirm")
    private FreemarkerTemplate template;

    @Autowired
    @Qualifier("registration")
    private MailTokenConfiguration mailTokenConfiguration;

    @Override
    FreemarkerTemplate getTemplate() {
        return template;
    }

    @Override
    String getTokenName() {
        return "registration.confirm";
    }

    @Override
    MailTokenConfiguration getMailTokenConfiguration() {
        return mailTokenConfiguration;
    }

    @Bean(name = "registration")
    @ConfigurationProperties(prefix = "askdog.mail.registration")
    MailTokenConfiguration mailTokenConfiguration() {
        return new MailTokenConfiguration();
    }
}
