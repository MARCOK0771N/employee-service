package com.employee.employee.dto;

import com.employee.employee.entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeInsertResponse {

    private List<EmployeeEntity> inserted;
    private List<EmployeeInsertError> errors;

}
