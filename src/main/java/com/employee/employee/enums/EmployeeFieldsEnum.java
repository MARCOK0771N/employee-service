package com.employee.employee.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeFieldsEnum {

    ID("id"),
    NAME("name"),
    SECOND_NAME("secondName"),
    LAST_NAME("lastName"),
    SECOND_LAST_NAME("secondLastName"),
    GENDER("gender"),
    AGE("age"),
    BIRTH_DATE("birthDate"),
    POSITION("position"),
    CREATION_DATE("creationDate"),
    ACTIVE("active");

    private final String fieldName;

}
