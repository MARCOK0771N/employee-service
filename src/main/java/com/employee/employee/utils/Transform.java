package com.employee.employee.utils;

import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeResponse;
import com.employee.employee.entity.EmployeeEntity;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Transform {

    private Transform(){}

    public static final UnaryOperator<String> cleanParamQuery = value ->
            Optional.ofNullable(value).map(String::trim).filter(Predicate.not(String::isEmpty)).orElse(null);

    public static final Function<EmployeeInsertRequest, EmployeeEntity> toEntity =
            req -> EmployeeEntity.builder()
                    .name(cleanParamQuery.apply(req.getName()))
                    .secondName(cleanParamQuery.apply(req.getSecondName()))
                    .lastName(cleanParamQuery.apply(req.getLastName()))
                    .secondLastName(cleanParamQuery.apply(req.getSecondLastName()))
                    .age(req.getAge())
                    .gender(req.getGender())
                    .birthDate(req.getBirthDate())
                    .position(req.getPosition())
                    .active(req.getActive())
                    .build();

    public static final Function<EmployeeEntity, EmployeeResponse> toResponse = entity ->
            EmployeeResponse.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .secondName(entity.getSecondName())
                    .lastName(entity.getLastName())
                    .secondLastName(entity.getSecondLastName())
                    .age(entity.getAge())
                    .gender(entity.getGender())
                    .birthDate(entity.getBirthDate())
                    .position(entity.getPosition())
                    .active(entity.getActive())
                    .build();
}
