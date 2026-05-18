package com.employee.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeUpdateRequest {

    @Schema(description = "First name")
    private String name;
    @Schema(description = "Second name")
    private String secondName;
    @Schema(description = "Last name")
    private String lastName;
    @Schema(description = "Second last name")
    private String secondLastName;
    @Schema(description = "Age of the employee")
    private Integer age;
    @Schema(description = "Gender")
    private String gender;

    @Schema(description = "Birth date in dd-MM-yyyy format")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Schema(description = "Birth date in dd-MM-yyyy format")
    private String position;
    @Schema(description = "Active status")
    private Boolean active;


    public boolean hasUpdates() {
        return Stream.of(name, secondName, lastName, secondLastName, age, gender, birthDate, position, active).anyMatch(Objects::nonNull);
    }
}
