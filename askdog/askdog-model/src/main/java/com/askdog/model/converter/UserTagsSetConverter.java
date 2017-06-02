package com.askdog.model.converter;

import com.askdog.model.entity.inner.user.UserTag;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.EnumSet;

public class UserTagsSetConverter extends JsonTypeReferenceConverter<EnumSet<UserTag>> {

    private TypeReference<EnumSet<UserTag>> typeReference;

    {
        typeReference = new TypeReference<EnumSet<UserTag>>() {};
    }

    @Override
    protected TypeReference<EnumSet<UserTag>> typeReference() {
        return typeReference;
    }
}