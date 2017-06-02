package com.askdog.model.converter;

import com.askdog.model.entity.inner.user.UserStatus;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.EnumSet;

public class UserStatusSetConverter extends JsonTypeReferenceConverter<EnumSet<UserStatus>> {

    private TypeReference<EnumSet<UserStatus>> typeReference;

    {
        typeReference = new TypeReference<EnumSet<UserStatus>>() {};
    }

    @Override
    protected TypeReference<EnumSet<UserStatus>> typeReference() {
        return typeReference;
    }
}