package com.employee.employee.utils;

import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeResponse;
import com.employee.employee.entity.EmployeeEntity;
import com.employee.employee.enums.EmployeeFieldsEnum;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Transform {

    private Transform(){}

    public static Pageable toPageable(Integer page, Integer size, String sort, String direction){
        String sortBy = Optional.ofNullable(sort).map(ValidationUtils.sortFieldValidator).map(EmployeeFieldsEnum::valueOf).map(EmployeeFieldsEnum::getFieldName).orElse(EmployeeFieldsEnum.ID.getFieldName());
        Sort.Direction sortDirection = Optional.ofNullable(direction).map(ValidationUtils.directionValidator).map(Sort.Direction::fromString).orElse(Sort.Direction.ASC);
        Optional.ofNullable(size).ifPresent(ValidationUtils.pageSizeValidator::apply);
        return PageRequest.of(Optional.ofNullable(page).orElse(0), Optional.ofNullable(size).orElse(10),  Sort.by(sortDirection, sortBy));
    }

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
