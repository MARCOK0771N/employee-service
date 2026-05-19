package com.employee.employee.service;

import com.employee.employee.dto.*;
import com.employee.employee.entity.EmployeeEntity;
import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.repository.EmployeeRepository;
import com.employee.employee.utils.EmployeeConstants;
import com.employee.employee.utils.Transform;
import com.employee.employee.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.employee.employee.utils.EmployeeConstants.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        log.info(EmployeeConstants.LOG_FETCH_ALL);
        Page<EmployeeEntity> employees = employeeRepository.findAll(pageable);
        log.info("Total employees fetched: {}", employees.getTotalElements());
        return employees.map(Transform.toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        return Transform.toResponse.apply(getEmployeeEntityById(id));
    }

    private EmployeeEntity getEmployeeEntityById(Long id) {
        log.info(LOG_FETCH_BY_ID, id);
        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException(id));
    }

    @Transactional
    @Override
    public EmployeeInsertResponse saveEmployees(List<EmployeeInsertRequest> request) {

        log.info("Starting employee insert process, total requests: {}", request.size());

        List<EmployeeEntity> inserted = new ArrayList<>();
        List<EmployeeInsertError> errors = new ArrayList<>();

        for (EmployeeInsertRequest req : request) {

            log.debug("Processing employee: {} {}", req.getName(), req.getLastName());

            try {

                String name = Transform.cleanParamQuery.apply(req.getName());
                String secondName = Transform.cleanParamQuery.apply(req.getSecondName());
                String lastName = Transform.cleanParamQuery.apply(req.getLastName());
                String secondLastName = Transform.cleanParamQuery.apply(req.getSecondLastName());

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
                        log.warn(EmployeeConstants.ERROR_DUPLICATE_ACTIVE, req.getName(), req.getLastName());
                    }
                    continue;
                }

                EmployeeEntity employee = Transform.toEntity.apply(req);
                inserted.add(employeeRepository.saveAndFlush(employee));
                log.info("Inserted new employee: {} {}", employee.getName(), employee.getLastName());

            } catch (Exception e) {
                errors.add(new EmployeeInsertError(req, "Unexpected error: " + e.getMessage()));
                log.error(EmployeeConstants.ERROR_UNEXPECTED, req.getName(), req.getLastName(), e.getMessage(), e);
            }
        }

        return EmployeeInsertResponse.builder().inserted(inserted).errors(errors).build();
    }


    @Transactional
    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeUpdateRequest request) {

        log.info("Updating employee id={} with request={}", id, request);

        EmployeeEntity existing = this.getEmployeeEntityById(id);

        log.debug("Applying updates to employee id={}", id);

        Optional.ofNullable(request.getActive()).ifPresent(existing::setActive);
        Optional.ofNullable(request.getAge()).map(ValidationUtils.ageValidator).ifPresent(existing::setAge);
        Optional.ofNullable(request.getGender()).map(Transform.cleanParamQuery).map(ValidationUtils.genderValidator).ifPresent(existing::setGender);
        Optional.ofNullable(request.getName()).map(Transform.cleanParamQuery).ifPresent(existing::setName);
        Optional.ofNullable(request.getSecondName()).map(Transform.cleanParamQuery).ifPresent(existing::setSecondName);
        Optional.ofNullable(request.getLastName()).map(Transform.cleanParamQuery).ifPresent(existing::setLastName);
        Optional.ofNullable(request.getSecondLastName()).map(Transform.cleanParamQuery).ifPresent(existing::setSecondLastName);
        Optional.ofNullable(request.getBirthDate()).ifPresent(existing::setBirthDate);
        Optional.ofNullable(request.getPosition()).map(Transform.cleanParamQuery).ifPresent(existing::setPosition);

        EmployeeEntity updated = employeeRepository.saveAndFlush(existing);
        log.info(LOG_UPDATE_SUCCESS, updated.getId());
        return Transform.toResponse.apply(updated);
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        log.info("Deleting employee id={}", id);
        this.getEmployeeEntityById(id);
        employeeRepository.deleteById(id);
        log.info(LOG_DELETE_SUCCESS, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EmployeeResponse> searchByName(String name, Pageable pageable) {
        return employeeRepository.findByNameContainingIgnoreCase(name, pageable).map(Transform.toResponse);
    }

}
