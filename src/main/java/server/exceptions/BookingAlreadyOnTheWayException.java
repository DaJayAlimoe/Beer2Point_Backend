package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingAlreadyOnTheWayException extends Exception {

    private final Long employeeId;

    public BookingAlreadyOnTheWayException(Long employeeId) {
        super(String.format("Order with number %d on the way", employeeId));

        this.employeeId = employeeId;
    }
}
