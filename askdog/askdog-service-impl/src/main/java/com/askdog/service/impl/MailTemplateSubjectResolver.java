package com.askdog.service.impl;

import com.askdog.common.FreemarkerTemplate;
import com.askdog.model.entity.Template;
import com.askdog.model.entity.inner.template.MailTemplateContent;

import java.util.function.Function;

public class MailTemplateSubjectResolver implements Function<FreemarkerTemplate, String> {

    @Override
    public String apply(FreemarkerTemplate template) {
        Template source = (Template) template.getSource();
        MailTemplateContent content = (MailTemplateContent) source.getContent();
        return content.getSubject();
    }
}
