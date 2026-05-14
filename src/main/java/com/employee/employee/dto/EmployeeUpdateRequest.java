package com.employee.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String name;
    private String secondName;
    private String lastName;
    private String secondLastName;
    private Integer age;
    private String gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    private String position;
    private Boolean active;


    public boolean hasUpdates() {
        return Stream.of(name, secondName, lastName, secondLastName, age, gender, birthDate, position, active).anyMatch(Objects::nonNull);
    }
}
