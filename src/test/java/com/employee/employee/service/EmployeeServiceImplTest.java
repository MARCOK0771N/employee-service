package com.employee.employee.service;

import com.employee.employee.dto.EmployeeInsertRequest;
import com.employee.employee.dto.EmployeeInsertResponse;
import com.employee.employee.dto.EmployeeResponse;
import com.employee.employee.dto.EmployeeUpdateRequest;
import com.employee.employee.entity.EmployeeEntity;
import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void testFindById() {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setId(1L);
        emp.setName("Marco");

        when(repository.findById(1L)).thenReturn(Optional.of(emp));

        EmployeeResponse result = employeeService.getEmployeeById(1L);

        assertEquals("Marco", result.getName());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetAllEmployees_success() {
        EmployeeEntity emp1 = new EmployeeEntity();
        emp1.setId(1L);
        emp1.setName("Marco");

        EmployeeEntity emp2 = new EmployeeEntity();
        emp2.setId(2L);
        emp2.setName("Antonio");

        Page<EmployeeEntity> pageResult = new PageImpl<>(List.of(emp1, emp2));
        when(repository.findAll(any(Pageable.class))).thenReturn(pageResult);

        Page<EmployeeResponse> result = employeeService.getAllEmployees(PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertEquals("Marco", result.getContent().get(0).getName());
        assertEquals("Antonio", result.getContent().get(1).getName());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }



    @Test
    void testSaveEmployees_success() {

        EmployeeInsertRequest req = new EmployeeInsertRequest();
        req.setName("Marco");
        req.setSecondName("Antonio");
        req.setLastName("Hernandez");
        req.setSecondLastName("Ramirez");
        req.setBirthDate(LocalDate.of(1990, 5, 14));

        EmployeeEntity entity = new EmployeeEntity();
        entity.setId(1L);
        entity.setName("Marco");
        entity.setSecondName("Antonio");
        entity.setLastName("Hernandez");
        entity.setSecondLastName("Ramirez");
        entity.setBirthDate(LocalDate.of(1990, 5, 14));
        entity.setActive(true);

        when(repository.findByNameAndSecondNameAndLastNameAndSecondLastNameAndBirthDate(
                "Marco", "Antonio", "Hernandez", "Ramirez", LocalDate.of(1990, 5, 14)))
                .thenReturn(Optional.empty());

        when(repository.saveAndFlush(any(EmployeeEntity.class))).thenReturn(entity);

        EmployeeInsertResponse response = employeeService.saveEmployees(List.of(req));

        assertEquals(1, response.getInserted().size());
        assertEquals("Marco", response.getInserted().get(0).getName());
        assertEquals("Antonio", response.getInserted().get(0).getSecondName());
        verify(repository, times(1)).saveAndFlush(any(EmployeeEntity.class));
    }

    @Test
    void testSaveEmployees_duplicateActive() {
        EmployeeInsertRequest req = new EmployeeInsertRequest();
        req.setName("Marco");
        req.setSecondName("Antonio");
        req.setLastName("Hernandez");
        req.setSecondLastName("Ramirez");
        req.setBirthDate(LocalDate.of(1990, 5, 14));

        EmployeeEntity existing = new EmployeeEntity();
        existing.setName("Marco");
        existing.setSecondName("Antonio");
        existing.setLastName("Hernandez");
        existing.setSecondLastName("Ramirez");
        existing.setBirthDate(LocalDate.of(1990, 5, 14));
        existing.setActive(true);

        when(repository.findByNameAndSecondNameAndLastNameAndSecondLastNameAndBirthDate(
                "Marco", "Antonio", "Hernandez", "Ramirez", LocalDate.of(1990, 5, 14)))
                .thenReturn(Optional.of(existing));

        EmployeeInsertResponse response = employeeService.saveEmployees(List.of(req));

        assertEquals(0, response.getInserted().size());
        assertEquals(1, response.getErrors().size());
        assertEquals("Duplicate employee already active", response.getErrors().get(0).getError());
    }

    @Test
    void testUpdateEmployee_success() {
        Long id = 1L;

        EmployeeEntity existing = new EmployeeEntity();
        existing.setId(id);
        existing.setName("Marco");
        existing.setSecondName("Antonio");
        existing.setLastName("Hernandez");
        existing.setSecondLastName("Ramirez");
        existing.setBirthDate(LocalDate.of(1990, 5, 14));
        existing.setActive(true);

        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();

        employeeUpdateRequest.setName("Marco");
        employeeUpdateRequest.setSecondName("Antonio");
        employeeUpdateRequest.setLastName("Hernandez");
        employeeUpdateRequest.setSecondLastName("Ramirez");
        employeeUpdateRequest.setBirthDate(LocalDate.of(1990, 5, 14));
        employeeUpdateRequest.setPosition("Senior Java Developer");
        employeeUpdateRequest.setActive(true);

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.saveAndFlush(any(EmployeeEntity.class))).thenReturn(existing);

        EmployeeResponse result = employeeService.updateEmployee(id, employeeUpdateRequest);

        assertEquals("Senior Java Developer", result.getPosition());
        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).saveAndFlush(any(EmployeeEntity.class));
    }

    @Test
    void testSearchEmployees_success() {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setId(1L);
        emp.setName("Marco");

        Page<EmployeeEntity> pageResult = new PageImpl<>(List.of(emp));
        when(repository.findByNameContainingIgnoreCase(eq("Marco"), any(Pageable.class))).thenReturn(pageResult);

        Page<EmployeeResponse> result = employeeService.searchByName("Marco", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Marco", result.getContent().get(0).getName());
        verify(repository, times(1)).findByNameContainingIgnoreCase(eq("Marco"), any(Pageable.class));
    }


    @Test
    void testDeleteEmployee_success() {
        Long id = 1L;

        EmployeeEntity existing = new EmployeeEntity();
        existing.setId(id);
        existing.setName("Marco");
        existing.setActive(true);

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        employeeService.deleteEmployee(id);

        verify(repository, times(1)).findById(id);
        verify(repository, times(1)).deleteById(id);
    }


    @Test
    void testUpdateEmployee_notFound() {
        Long id = 99L;
        EmployeeUpdateRequest updated = new EmployeeUpdateRequest();
        updated.setName("Marco");

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(id, updated));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).save(any(EmployeeEntity.class));
    }


    @Test
    void testDeleteEmployee_notFound() {
        Long id = 99L;

        when(repository.findById(id)).thenThrow(EmployeeNotFoundException.class);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(id));
        verify(repository, times(1)).findById(id);
        verify(repository, never()).delete(any(EmployeeEntity.class));
    }


}