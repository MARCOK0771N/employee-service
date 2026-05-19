package com.employee.employee.contoller;

import com.employee.employee.dto.*;
import com.employee.employee.enums.EmployeeFieldsEnum;
import com.employee.employee.exception.InvalidEmployeeDataException;
import com.employee.employee.service.EmployeeService;
import com.employee.employee.utils.Transform;
import com.employee.employee.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     ** The exercise doesn't mention it, but the most convenient thing would be for it to be paginated according to the number of records it might have.
    * */
    @Operation(summary = "Get all employees")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = "application/json")
    public Page<EmployeeResponse> getAllEmployees(@Parameter(description = "Page number (0 = first)")  @RequestParam(required = false) Integer page, @Parameter(description = "Page size (must be between 1 and 100)") @RequestParam(required = false) Integer size,
                                                  @Parameter(description = "Sort field:  ID, NAME, SECOND_NAME, LAST_NAME, SECOND_LAST_NAME, GENDER, AGE, BIRTH_DATE, POSITION, CREATION_DATE, ACTIVE") @RequestParam(required = false) String sort, @Parameter(description = "Sort direction: ASC or DESC")  @RequestParam(required = false) String direction) {
        return employeeService.getAllEmployees(Transform.toPageable(page, size, sort, direction));
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee successfully created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeInsertResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo empleado", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class))
    )
    @PostMapping(produces = "application/json")
    public ResponseEntity<EmployeeInsertResponse> createEmployees( @Valid @RequestBody EmployeeCreateRequest request) {
        List<EmployeeInsertRequest> employees = Optional.ofNullable(request).map(EmployeeCreateRequest::getEmployees).filter(Predicate.not(Collection::isEmpty)).orElseThrow(()-> new InvalidEmployeeDataException("At least employee record must one be provided for save"));
        EmployeeInsertResponse response = employeeService.saveEmployees(employees);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update an existing employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated employee data", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeUpdateRequest.class))
    )
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateRequest request) {
        if (Objects.isNull(request) || !request.hasUpdates()) {
            throw new InvalidEmployeeDataException("At least one field must be provided for update");
        }
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    @Operation(summary = "Delete an employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }


    /**
     ** The exercise doesn't mention it, but the most convenient thing would be for it to be paginated according to the number of records it might have.
     * */
    @Operation(summary = "Search employees by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponse.class))),
            @ApiResponse(responseCode = "404", description = "No employees found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = "/search", produces = "application/json")
    public Page<EmployeeResponse> searchByName(@RequestParam String name, @Parameter(description = "Page number (0 = first)")  @RequestParam(required = false) Integer page, @Parameter(description = "Page size (must be between 1 and 100)") @RequestParam(required = false) Integer size,
                                               @Parameter(description = "Sort field:  ID, NAME, SECOND_NAME, LAST_NAME, SECOND_LAST_NAME, GENDER, AGE, BIRTH_DATE, POSITION, CREATION_DATE, ACTIVE") @RequestParam(required = false) String sort, @Parameter(description = "Sort direction: ASC or DESC")  @RequestParam(required = false) String direction) {
        return employeeService.searchByName(name, Transform.toPageable(page, size, sort, direction));
    }
}
