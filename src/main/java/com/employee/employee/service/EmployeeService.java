package com.employee.employee.service;

import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeInsertResponse;
import com.employee.employee.dto.EmployeeUpdateRequest;
import com.employee.employee.entity.EmployeeEntity;
import java.util.List;

public interface EmployeeService {

    List<EmployeeEntity> getAllEmployees();

    EmployeeEntity getEmployeeById(Long id);

    EmployeeInsertResponse saveEmployees(List<EmployeeInsertRequest> request);

    EmployeeEntity updateEmployee(Long id, EmployeeUpdateRequest request) ;

    void deleteEmployee(Long id);

    List<EmployeeEntity> searchByName(String name);

}
