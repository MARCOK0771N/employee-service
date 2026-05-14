package com.employee.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeInsertRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    /**
    ** this fields may not be mandatory
    * */
    private String secondName;
    private String secondLastName;

    @NotBlank(message = "Last name (father) is mandatory")
    private String lastName;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 110, message = "Age must be less than 110")
    private Integer age;

    @Pattern(regexp = "Male|Female", message = "Gender must be Male or Female")
    private String gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotBlank(message = "Position is mandatory")
    private String position;

    @NotNull(message = "Active status is required")
    private Boolean active;
}
