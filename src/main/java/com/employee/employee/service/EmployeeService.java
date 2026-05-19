package com.employee.employee.service;

import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeInsertResponse;
import com.employee.employee.dto.EmployeeResponse;
import com.employee.employee.dto.EmployeeUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    EmployeeResponse getEmployeeById(Long id);

    EmployeeInsertResponse saveEmployees(List<EmployeeInsertRequest> request);

    EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) ;

    void deleteEmployee(Long id);

    Page<EmployeeResponse> searchByName(String name, Pageable pageable);

}
