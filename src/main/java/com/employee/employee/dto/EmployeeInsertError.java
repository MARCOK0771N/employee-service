package com.employee.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeInsertError {

    @Schema(description = "record failed or duplicate")
    private EmployeeInsertRequest request;
    @Schema(description = "Error description")
    private String error;

}
