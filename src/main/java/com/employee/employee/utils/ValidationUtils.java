package com.employee.employee.utils;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.employee.employee.utils.EmployeeConstants.*;

public class ValidationUtils {

    private ValidationUtils(){}

    public static final UnaryOperator<Integer> ageValidator = ValidationUtils.rangeValidator(MIN_AGE, MAX_AGE, MSG_AGE_ERROR);

    public static final UnaryOperator<String> genderValidator = ValidationUtils.regexValidator(GENDER, MSG_GENDER_ERROR);

    public static UnaryOperator<String> regexValidator(String regex, String message) {
        return value -> {
            if (value == null || !value.matches(regex)) {
                throw new IllegalArgumentException(message);
            }
            return value;
        };
    }

    public static UnaryOperator<Integer> rangeValidator(int min, int max, String message) {
        return value -> {
            if (value == null || value < min || value > max) {
                throw new IllegalArgumentException(message);
            }
            return value;
        };
    }

    public static <T> Function<T, T> notEmptyValidator(String message) {
        return value -> {
            if (value == null || value.toString().isEmpty()) {
                throw new IllegalArgumentException(message);
            }
            return value;
        };
    }

}
