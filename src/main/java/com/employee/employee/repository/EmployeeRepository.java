package com.employee.employee.repository;

import com.employee.employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Page<EmployeeEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<EmployeeEntity> findByNameAndSecondNameAndLastNameAndSecondLastNameAndBirthDate(String name, String secondName, String lastName, String secondLastName, LocalDate birthDate);

}