package com.askdog.model.converter;

import com.askdog.model.entity.inner.template.Content;

public class TemplateContentConverter extends JsonConverter<Content> {

    @Override
    protected Class<Content> type() {
        return Content.class;
    }
}
