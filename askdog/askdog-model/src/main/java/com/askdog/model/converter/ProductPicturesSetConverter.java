package com.askdog.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class ProductPicturesSetConverter extends JsonTypeReferenceConverter<List<Long>> {

    private TypeReference<List<Long>> typeReference;

    {
        typeReference = new TypeReference<List<Long>>() {
        };
    }

    @Override
    protected TypeReference<List<Long>> typeReference() {
        return typeReference;
    }
}
