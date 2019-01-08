package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingAlreadyConfirmedException extends Exception {

    private final String orderNumber;

    public BookingAlreadyConfirmedException(Long orderId) {
        super(String.format("Order with number %d already confirmed", orderId));

        this.orderNumber = orderId.toString();
    }
}
