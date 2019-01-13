package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class EmployeeTokenWrongException extends Exception{

    private final String token;

    public EmployeeTokenWrongException(String token) {
        super(String.format("Token %s does not match the Employee number. Check your employee card", token));

        this.token = token;
    }
}
