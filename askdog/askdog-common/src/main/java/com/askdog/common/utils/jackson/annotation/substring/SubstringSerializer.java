package com.askdog.common.utils.jackson.annotation.substring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.util.Assert;

import java.io.IOException;

public class SubstringSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private int from;
    private int size;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Assert.isTrue(from >= 0 && from <= value.length());

        if (value == null) {
            gen.writeNull();
            return;
        }

        int to = from + size > value.length() ? value.length() : from + size;

        gen.writeString(value.substring(from, to));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {

            Substring substring = beanProperty.getAnnotation(Substring.class);

            if (substring == null) {
                substring = beanProperty.getContextAnnotation(Substring.class);
            }

            if (substring != null) {
                this.from = substring.from();
                this.size = substring.size();
            }
        }

        return this;
    }

}