package com.employee.employee.contoller;

import com.employee.employee.dto.*;
import com.employee.employee.exception.InvalidEmployeeDataException;
import com.employee.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    private EmployeeService employeeService;
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        employeeService = Mockito.mock(EmployeeService.class);
        controller = new EmployeeController(employeeService);
    }

    @Test
    void testGetAllEmployees() {
        EmployeeResponse emp = EmployeeResponse.builder().id(1L).name("Marco").build();
        when(employeeService.getAllEmployees()).thenReturn(List.of(emp));

        List<EmployeeResponse> result = controller.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("Marco", result.get(0).getName());
        verify(employeeService).getAllEmployees();
    }

    @Test
    void testGetEmployeeById() {
        EmployeeResponse emp = EmployeeResponse.builder().id(1L).name("Marco").build();
        when(employeeService.getEmployeeById(1L)).thenReturn(emp);

        var response = controller.getEmployeeById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Marco", response.getBody().getName());
        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void testCreateEmployeesSuccess() {
        EmployeeInsertRequest req = EmployeeInsertRequest.builder().name("Marco").lastName("Lopez").build();
        EmployeeCreateRequest createRequest = new EmployeeCreateRequest(List.of(req));
        EmployeeInsertResponse insertResponse = EmployeeInsertResponse.builder().inserted(List.of()).errors(List.of()).build();

        when(employeeService.saveEmployees(anyList())).thenReturn(insertResponse);

        var response = controller.createEmployees(createRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(insertResponse, response.getBody());
        verify(employeeService).saveEmployees(anyList());
    }

    @Test
    void testCreateEmployeesInvalidRequestThrowsException() {
        EmployeeCreateRequest emptyRequest = new EmployeeCreateRequest(List.of());

        assertThrows(InvalidEmployeeDataException.class, () -> controller.createEmployees(emptyRequest));
    }

    @Test
    void testUpdateEmployeeSuccess() {
        EmployeeUpdateRequest updateReq = new EmployeeUpdateRequest();
        updateReq.setName("Updated");
        EmployeeResponse updated = EmployeeResponse.builder().id(1L).name("Updated").build();

        when(employeeService.updateEmployee(eq(1L), any(EmployeeUpdateRequest.class))).thenReturn(updated);

        var response = controller.updateEmployee(1L, updateReq);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated", response.getBody().getName());
        verify(employeeService).updateEmployee(eq(1L), any(EmployeeUpdateRequest.class));
    }

    @Test
    void testUpdateEmployeeInvalidThrowsException() {
        EmployeeUpdateRequest emptyReq = new EmployeeUpdateRequest(); // no updates

        assertThrows(InvalidEmployeeDataException.class, () -> controller.updateEmployee(1L, emptyReq));
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeService).deleteEmployee(1L);

        var response = controller.deleteEmployee(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(employeeService).deleteEmployee(1L);
    }

    @Test
    void testSearchByName() {
        EmployeeResponse emp = EmployeeResponse.builder().id(1L).name("Marco").build();
        when(employeeService.searchByName("Marco")).thenReturn(List.of(emp));

        List<EmployeeResponse> result = controller.searchByName("Marco");

        assertEquals(1, result.size());
        assertEquals("Marco", result.get(0).getName());
        verify(employeeService).searchByName("Marco");
    }
}
