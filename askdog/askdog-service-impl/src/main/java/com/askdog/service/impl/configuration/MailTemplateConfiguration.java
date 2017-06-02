package com.askdog.service.impl.configuration;

import com.askdog.common.FreemarkerTemplate;
import com.askdog.common.TemplateConfiguration;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static freemarker.cache.NullCacheStorage.INSTANCE;
import static java.lang.String.format;

@org.springframework.context.annotation.Configuration
public class MailTemplateConfiguration implements TemplateConfiguration {

    @Autowired
    private MailTemplateLoader mailTemplateLoader;

    private Configuration configuration;

    @PostConstruct
    private void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        // cache the templates in loader, that because the key of cache storage is not accessible.
        // and the key and value also not serializable, so can not be cluster. :(
        configuration.setTemplateLoader(mailTemplateLoader);
        configuration.setCacheStorage(INSTANCE);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setLogTemplateExceptions(false);
    }

    @Override
    public FreemarkerTemplate findTemplate(String value) throws IOException {
        return new FreemarkerTemplate(configuration, value, (locale) -> mailTemplateLoader.findTemplateSource(format("%s_%s", value, locale)));
    }

}
