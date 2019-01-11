package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.domain.dtos.EmployeeCreateDTO;
import server.domain.dtos.IdDTO;
import server.domain.entities.Employee;
import server.domain.repositories.EmployeeRepository;
import server.domain.repositories.BookingRepository;
import server.exceptions.EmployeeNotFoundException;
import server.service.LogicalService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/Employee")
@Api(value = "/v1/Employee", tags = "Employees")
public class EmployeeController {

    private final LogicalService logicalService;

    private final EmployeeRepository employeeRepository;

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public EmployeeController(LogicalService logicalService, EmployeeRepository employeeRepository) {
        this.logicalService = logicalService;
        this.employeeRepository = employeeRepository;
    }

    // Get all employees
    @ApiOperation(value = "Get all employees", response = Employee.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved employees"),
    })
    @GetMapping
    public List<Employee> getEmployees() {
        return logicalService.getEmployees();
    }

    // Get Employee by employee Id
    @ApiOperation(value = "Get a employee by Id", response = Employee.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved Employee"),
            @ApiResponse(code = 404, message = "Employee is not found")
    })
    @GetMapping(value = "/{id:[\\d]+}")
    public Employee getEmployee(@PathVariable("id") Long employeeId) throws EmployeeNotFoundException {

        return employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
    }

    // Delete a employee by Id
    @ApiOperation(value = "Delete a employee by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted employee"),
            @ApiResponse(code = 404, message = "employee is not found")
    })
    @DeleteMapping("/{id:[\\d]+}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable("id") Long employeeId) throws EmployeeNotFoundException {
        logicalService.deleteEmployee(employeeId);
    }

    // create a Employee by DTO object
    @ApiOperation(value = "Create a employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created customer"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO addEmployee(@Valid @RequestBody EmployeeCreateDTO employeeCreateDTO) {
        return logicalService.createEmployee(employeeCreateDTO);
    }
}