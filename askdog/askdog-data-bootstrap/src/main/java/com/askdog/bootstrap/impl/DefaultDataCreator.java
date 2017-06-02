package com.askdog.bootstrap.impl;

import com.askdog.bootstrap.core.DataCreator;
import com.askdog.dao.repository.TemplateRepository;
import com.askdog.dao.repository.UserRepository;
import com.askdog.model.entity.Template;
import com.askdog.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataCreator implements DataCreator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public DataCreator user(User user) {
        userRepository.save(user);
        return this;
    }

    @Override
    public DataCreator template(Template template) {
        templateRepository.save(template);
        return this;
    }

}