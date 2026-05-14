package com.employee.employee.service;

import com.employee.employee.dto.EmployeeInsertError;
import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeInsertResponse;
import com.employee.employee.dto.EmployeeUpdateRequest;
import com.employee.employee.entity.EmployeeEntity;
import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<EmployeeEntity> getAllEmployees() {
        log.info("Fetching all employees from database");
        List<EmployeeEntity> employees = employeeRepository.findAll();
        log.info("Total employees fetched: {}", employees.size());
        return employees;
    }


    @Transactional(readOnly = true)
    @Override
    public EmployeeEntity getEmployeeById(Long id) {
        log.info("Fetching employee with id={}", id);
        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
    }

    @Override
    public EmployeeInsertResponse saveEmployees(List<EmployeeInsertRequest> request) {

        log.info("Starting employee insert process, total requests: {}", request.size());

        List<EmployeeEntity> inserted = new ArrayList<>();
        List<EmployeeInsertError> errors = new ArrayList<>();

        for (EmployeeInsertRequest req : request) {

            log.debug("Processing employee: {} {}", req.getName(), req.getLastName());

            try {

                String name = cleanParamQuery.apply(req.getName());
                String secondName = cleanParamQuery.apply(req.getSecondName());
                String lastName = cleanParamQuery.apply(req.getLastName());
                String secondLastName = cleanParamQuery.apply(req.getSecondLastName());

                Optional<EmployeeEntity> existingOpt = employeeRepository.findByNameAndSecondNameAndLastNameAndSecondLastNameAndBirthDate( name, secondName, lastName, secondLastName, req.getBirthDate());

                if (existingOpt.isPresent()) {
                    EmployeeEntity existing = existingOpt.get();
                    log.info("Found existing employee with id={} active={}", existing.getId(), existing.getActive());

                    if (Boolean.FALSE.equals(existing.getActive())) {
                        existing.setActive(true);
                        existing.setCreationDate(LocalDateTime.now());
                        inserted.add(employeeRepository.save(existing));
                        log.info("Reactivated employee id={}", existing.getId());
                    } else {
                        errors.add(new EmployeeInsertError(req, "Duplicate employee already active"));
                        log.warn("Duplicate active employee: {} {}", req.getName(), req.getLastName());
                    }
                    continue;
                }

                EmployeeEntity employee = toEntity.apply(req);
                inserted.add(employeeRepository.saveAndFlush(employee));
                log.info("Inserted new employee: {} {}", employee.getName(), employee.getLastName());

            } catch (Exception e) {
                errors.add(new EmployeeInsertError(req, "Unexpected error: " + e.getMessage()));
                log.error("Error inserting employee {} {}: {}", req.getName(), req.getLastName(), e.getMessage(), e);
            }
        }

        return EmployeeInsertResponse.builder().inserted(inserted).errors(errors).build();
    }

    @Override
    public EmployeeEntity updateEmployee(Long id, EmployeeUpdateRequest request) {

        log.info("Updating employee id={} with request={}", id, request);

        EmployeeEntity existing = getEmployeeById(id);

        log.debug("Applying updates to employee id={}", id);

        Optional.ofNullable(request.getActive()).ifPresent(existing::setActive);
        Optional.ofNullable(request.getAge()).ifPresent(existing::setAge);
        Optional.ofNullable(request.getGender()).map(cleanParamQuery).ifPresent(existing::setGender);
        Optional.ofNullable(request.getName()).map(cleanParamQuery).ifPresent(existing::setName);
        Optional.ofNullable(request.getSecondName()).map(cleanParamQuery).ifPresent(existing::setSecondName);
        Optional.ofNullable(request.getLastName()).map(cleanParamQuery).ifPresent(existing::setLastName);
        Optional.ofNullable(request.getSecondLastName()).map(cleanParamQuery).ifPresent(existing::setSecondLastName);
        Optional.ofNullable(request.getBirthDate()).ifPresent(existing::setBirthDate);
        Optional.ofNullable(request.getPosition()).map(cleanParamQuery).ifPresent(existing::setPosition);

        EmployeeEntity updated = employeeRepository.saveAndFlush(existing);
        log.info("Employee updated successfully id={}", updated.getId());
        return updated;
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee id={}", id);
        getEmployeeById(id);
        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully id={}", id);
    }


    @Transactional(readOnly = true)
    @Override
    public List<EmployeeEntity> searchByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    private final UnaryOperator<String> cleanParamQuery = value ->
            Optional.ofNullable(value).map(String::trim).filter(Predicate.not(String::isEmpty)).orElse(null);

    private final Function<EmployeeInsertRequest, EmployeeEntity> toEntity =
            req -> EmployeeEntity.builder()
                    .name(cleanParamQuery.apply(req.getName()))
                    .secondName(cleanParamQuery.apply(req.getSecondName()))
                    .lastName(cleanParamQuery.apply(req.getLastName()))
                    .secondLastName(cleanParamQuery.apply(req.getSecondLastName()))
                    .age(req.getAge())
                    .gender(req.getGender())
                    .birthDate(req.getBirthDate())
                    .position(req.getPosition())
                    .active(req.getActive())
                    .build();





}
