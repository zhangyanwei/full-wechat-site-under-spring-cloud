package com.askdog.service.impl.mail;

import com.askdog.common.FreemarkerTemplate;
import com.askdog.model.entity.User;
import com.askdog.web.common.async.AsyncCaller;
import com.askdog.service.impl.MailTemplateSubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.PostConstruct;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.askdog.common.mail.MailSender.newMailSender;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Locale.SIMPLIFIED_CHINESE;

abstract class AbstractMail implements Mail {

    @Autowired
    private AsyncCaller asyncCaller;

    @Autowired
    @Qualifier("global")
    private JavaMailSenderImpl javaMailSender;

    @PostConstruct
    private void init() {
        javaMailSender.setUsername(getMailConfiguration().getUserName());
        javaMailSender.setPassword(getMailConfiguration().getPassword());
    }

    @Override
    public void send(User user, Map<String, Serializable> data) {
        String nick = getMailConfiguration().getNickName();

        asyncCaller.asyncCall(() -> {
            String senderAddress = "ASKDOG";
            try {
                senderAddress = new InternetAddress(nick + " <" + getMailConfiguration().getUserName() + ">").toString();
            } catch (AddressException e) {
                e.printStackTrace();
            }
            HashMap<String, Serializable> templateData = data != null ? newHashMap(data) : new HashMap<>();
            updateTemplateData(user, templateData);
            newMailSender().sender(javaMailSender)
                    .template(getTemplate())
                    .data(templateData)
                    .from(senderAddress)
                    .to(user.getEmail())
                    .subject(new MailTemplateSubjectResolver())
                    .send(SIMPLIFIED_CHINESE, getMailConfiguration().getRetryCount());
        });
    }

    void updateTemplateData(User user, Map<String, Serializable> data) {
        data.putIfAbsent("user", user);
    }

    abstract MailConfiguration getMailConfiguration();

    abstract FreemarkerTemplate getTemplate();

}
