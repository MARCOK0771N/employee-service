package com.employee.employee.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testAgeValidatorValid() {
        assertEquals(25, ValidationUtils.ageValidator.apply(25));
    }

    @Test
    void testAgeValidatorTooLowThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.ageValidator.apply(10));
        assertEquals("Age must be between 18 and 110", ex.getMessage());
    }

    @Test
    void testAgeValidatorTooHighThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.ageValidator.apply(120));
        assertEquals("Age must be between 18 and 110", ex.getMessage());
    }

    @Test
    void testGenderValidatorValid() {
        assertEquals("Male", ValidationUtils.genderValidator.apply("Male"));
        assertEquals("Female", ValidationUtils.genderValidator.apply("Female"));
    }

    @Test
    void testGenderValidatorInvalidThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.genderValidator.apply("Other"));
        assertEquals("Gender must be Male or Female", ex.getMessage());
    }

    @Test
    void testNotEmptyValidatorThrowsOnEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.notEmptyValidator("Must not be empty").apply(""));
        assertEquals("Must not be empty", ex.getMessage());
    }

    @Test
    void testNotEmptyValidatorThrowsOnNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationUtils.notEmptyValidator("Must not be empty").apply(null));
        assertEquals("Must not be empty", ex.getMessage());
    }
}
