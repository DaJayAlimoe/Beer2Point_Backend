package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class SeatNotFoundException extends Exception{

    private final String resource;

    public SeatNotFoundException(Long resource) {
        super(String.format("Seat with number %d not found", resource));

        this.resource = resource.toString();
    }

    public SeatNotFoundException(String resource) {
        super(String.format("Seat with token %s not found", resource));

        this.resource = resource;
    }
}
