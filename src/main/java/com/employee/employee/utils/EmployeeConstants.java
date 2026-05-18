package com.employee.employee.utils;

public class EmployeeConstants {

    private EmployeeConstants() {}

    public static final String LOG_FETCH_ALL = "Fetching all employees from database";
    public static final String LOG_FETCH_BY_ID = "Fetching employee with id={}";
    public static final String LOG_UPDATE_SUCCESS = "Employee updated successfully id={}";
    public static final String LOG_DELETE_SUCCESS = "Employee deleted successfully id={}";

    public static final String ERROR_DUPLICATE_ACTIVE = "Duplicate active employee: {} {}";
    public static final String ERROR_UNEXPECTED = "Error inserting employee {} {}: {}";

    public static final Integer MIN_AGE = 18;
    public static final Integer MAX_AGE = 110;
    public static final String MSG_AGE_ERROR = "Age must be between 18 and 110";
    public static final String MSG_GENDER_ERROR = "Gender must be Male or Female";
    public static final String GENDER = "Male|Female";

}
