package com.employee.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class EmployeeCreateRequest {

    @NotEmpty(message = "Employee list cannot be empty")
    @Valid
    private List<EmployeeInsertRequest> employees;
}