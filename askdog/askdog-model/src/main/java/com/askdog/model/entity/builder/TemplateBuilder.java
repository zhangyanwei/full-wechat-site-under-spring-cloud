package com.askdog.model.entity.builder;

import com.askdog.model.entity.Template;
import com.askdog.model.entity.inner.template.MailTemplateContent;

import java.util.Date;
import java.util.Locale;

public final class TemplateBuilder {

    public static MailTemplateBuilder mailTemplateBuilder() {
        return new MailTemplateBuilder();
    }

    public final static class MailTemplateBuilder {

        private String name;
        private String subject;
        private String content;
        private Locale language;
        private Date creationTime;
        private Date lastModifyTime;

        public MailTemplateBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MailTemplateBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailTemplateBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MailTemplateBuilder language(Locale language) {
            this.language = language;
            return this;
        }

        public MailTemplateBuilder creationTime(Date creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public MailTemplateBuilder lastModifyTime(Date lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
            return this;
        }

        public Template build() {

            MailTemplateContent content = new MailTemplateContent();
            content.setSubject(this.subject);
            content.setContent(this.content);

            Template mailTemplate = new Template();
            mailTemplate.setName(this.name);
            mailTemplate.setContent(content);
            mailTemplate.setLanguage(this.language);
            mailTemplate.setCreationTime(this.creationTime != null ? this.creationTime : new Date());
            mailTemplate.setLastModifyTime(this.lastModifyTime);
            return mailTemplate;
        }
    }

}
