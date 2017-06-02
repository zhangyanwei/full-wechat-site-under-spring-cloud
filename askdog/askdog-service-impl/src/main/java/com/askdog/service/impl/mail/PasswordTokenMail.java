package com.askdog.service.impl.mail;

import com.askdog.common.FreemarkerTemplate;
import com.askdog.common.TemplateSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PasswordTokenMail extends AbstractTokenMail {

    @TemplateSource(name = "password-recover")
    private FreemarkerTemplate template;

    @Autowired
    @Qualifier("password")
    private MailTokenConfiguration mailTokenConfiguration;

    @Override
    FreemarkerTemplate getTemplate() {
        return template;
    }

    @Override
    String getTokenName() {
        return "password.recover";
    }

    @Override
    MailTokenConfiguration getMailTokenConfiguration() {
        return mailTokenConfiguration;
    }

    @Bean(name = "password")
    @ConfigurationProperties(prefix = "askdog.mail.password")
    MailTokenConfiguration mailTokenConfiguration() {
        return new MailTokenConfiguration();
    }
}
