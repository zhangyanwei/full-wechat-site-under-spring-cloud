package com.askdog.common;

import java.io.IOException;

public interface TemplateConfiguration {
    FreemarkerTemplate findTemplate(String value) throws IOException;
}
