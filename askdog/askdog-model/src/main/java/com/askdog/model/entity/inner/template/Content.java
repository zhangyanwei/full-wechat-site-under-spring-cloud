package com.askdog.model.entity.inner.template;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(
        use = NAME,
        include = EXTERNAL_PROPERTY
)
@JsonSubTypes({
        @JsonSubTypes.Type(name = "mail", value = MailTemplateContent.class)
})
public abstract class Content implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
