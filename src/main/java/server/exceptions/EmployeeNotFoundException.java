package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends Exception {

    private final String employeeToken;

    public EmployeeNotFoundException(Long employeeId) {
        super(String.format("Could not find employee with number %d.", employeeId));

        this.employeeToken = employeeId.toString();
    }

    public EmployeeNotFoundException(String employeetoken) {
        super(String.format("Could not find employee with token %s.", employeetoken));

        this.employeeToken = employeetoken;
    }
}