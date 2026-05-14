package com.employee.employee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEntity {


    @Schema(description = "Unique identifier of the employee", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Employee name", example = "Marco")
    private String name;

    @Schema(description = "Employee second name", example = "Antonio")
    private String secondName;

    @Schema(description = "Employee last name", example = "Hernandez")
    private String lastName;

    @Schema(description = "Employee second last name", example = "Ramirez")
    private String secondLastName;

    @Schema(description = "Employee gender", example = "Male")
    private String gender;

    @Schema(description = "Employee age", example = "35")
    private Integer age;

    @Schema(description = "Employee birth date", example = "1990-05-14")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;

    @Schema(description = "Employee job position", example = "Senior Java Developer")
    private String position;

    @Schema(description = "Date when the employee was created in the system", example = "2026-05-13T22:45:51")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Schema(description = "Indicates if the employee is active", example = "true")
    private Boolean active;
}
