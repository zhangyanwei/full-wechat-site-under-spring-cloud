package com.askdog.model.converter;

import com.askdog.model.entity.Product.ProductTags;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.EnumSet;

public class ProductTagsSetConverter extends JsonTypeReferenceConverter<EnumSet<ProductTags>> {

    private TypeReference<EnumSet<ProductTags>> typeReference;

    {
        typeReference = new TypeReference<EnumSet<ProductTags>>() {
        };
    }

    @Override
    protected TypeReference<EnumSet<ProductTags>> typeReference() {
        return typeReference;
    }
}
