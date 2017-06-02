package com.askdog.model.converter;

import javax.persistence.AttributeConverter;

import static com.askdog.common.utils.Json.readValue;
import static com.askdog.common.utils.Json.writeValueAsString;

abstract class JsonConverter<T> implements AttributeConverter<T, String> {

    @Override
    public String convertToDatabaseColumn(T attribute) {
        return writeValueAsString(attribute);
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        return readValue(dbData, type());
    }

    abstract protected Class<T> type();
}
