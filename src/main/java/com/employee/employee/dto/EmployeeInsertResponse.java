package com.employee.employee.dto;

import com.employee.employee.entity.EmployeeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "List of records successful")
    private List<EmployeeEntity> inserted;
    @Schema(description = "List of records with error or duplicates")
    private List<EmployeeInsertError> errors;

}
