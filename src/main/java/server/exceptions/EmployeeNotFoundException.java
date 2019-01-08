package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends Exception {

    private final Long employeeId;

    public EmployeeNotFoundException(Long employeeId) {
        super(String.format("Could not find customer with numer %d.", employeeId));

        this.employeeId = employeeId;
    }
}