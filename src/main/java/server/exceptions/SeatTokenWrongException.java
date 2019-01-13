package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class SeatTokenWrongException extends Exception{

    private final String token;

    public SeatTokenWrongException(String token) {
        super(String.format("Token %s does not match the seat number", token));

        this.token = token;
    }
}
