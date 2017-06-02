package com.askdog.model.converter;

import com.askdog.model.security.Authority;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.EnumSet;

public class RoleSetConverter extends JsonTypeReferenceConverter<EnumSet<Authority.Role>> {

    private TypeReference<EnumSet<Authority.Role>> typeReference;

    {
        typeReference = new TypeReference<EnumSet<Authority.Role>>() {};
    }

    @Override
    protected TypeReference<EnumSet<Authority.Role>> typeReference() {
        return typeReference;
    }
}