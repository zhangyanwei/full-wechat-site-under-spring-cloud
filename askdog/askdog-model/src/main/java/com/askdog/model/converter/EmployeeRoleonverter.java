package com.askdog.model.converter;

import com.askdog.model.entity.StoreEmployee.EmployeeRole;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.EnumSet;

public class EmployeeRoleonverter extends JsonTypeReferenceConverter<EnumSet<EmployeeRole>> {

    private static TypeReference<EnumSet<EmployeeRole>> typeReference = new TypeReference<EnumSet<EmployeeRole>>() {
    };

    @Override
    protected TypeReference<EnumSet<EmployeeRole>> typeReference() {
        return typeReference;
    }
}
