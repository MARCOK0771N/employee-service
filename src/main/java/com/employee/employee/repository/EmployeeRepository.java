package com.employee.employee.repository;

import com.employee.employee.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByNameContainingIgnoreCase(String name);

    Optional<EmployeeEntity> findByNameAndSecondNameAndLastNameAndSecondLastNameAndBirthDate(String name, String secondName, String lastName, String secondLastName, LocalDate birthDate);

}