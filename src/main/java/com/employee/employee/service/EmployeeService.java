package com.employee.employee.service;

import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeInsertResponse;
import com.employee.employee.dto.EmployeeResponse;
import com.employee.employee.dto.EmployeeUpdateRequest;
import java.util.List;

public interface EmployeeService {

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    EmployeeInsertResponse saveEmployees(List<EmployeeInsertRequest> request);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) ;

    void deleteEmployee(Long id);

    List<EmployeeResponse> searchByName(String name);

}
