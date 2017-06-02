package com.askdog.service.impl.configuration;

import com.askdog.model.entity.Template;
import com.askdog.dao.repository.TemplateRepository;
import freemarker.cache.TemplateLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;

import static com.askdog.common.utils.Locales.fromValue;

@Component
public class MailTemplateLoader implements TemplateLoader {

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public Object findTemplateSource(String name) throws IOException {
        String[] nameInfo = name.split("_", 2);
        return templateRepository.findByNameAndLanguage(nameInfo[0], fromValue(nameInfo[1]))
                .orElseThrow(() -> new IOException("can not found the template: " + name));
    }

    @Override
    public long getLastModified(Object templateSource) {
        Template mailTemplate = (Template) templateSource;
        Date time = mailTemplate.getLastModifyTime();
        return time == null ? -1 : time.getTime();
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Template mailTemplate = (Template) templateSource;
        return new StringReader(mailTemplate.getContent().getContent());
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
    }

}
