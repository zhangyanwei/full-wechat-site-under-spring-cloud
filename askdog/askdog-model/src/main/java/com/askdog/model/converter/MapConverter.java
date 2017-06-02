package com.askdog.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class MapConverter extends JsonTypeReferenceConverter<Map> {

    private TypeReference<Map> typeReference;

    {
        typeReference = new TypeReference<Map>() {};
    }

    @Override
    protected TypeReference<Map> typeReference() {
        return typeReference;
    }
}