package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends Exception {

    private final String orderNumber;

    public BookingNotFoundException(String item) {
        super(String.format("Could not find order with Item %s.", item));

        this.orderNumber = item;
    }

    public BookingNotFoundException(Long orderId) {
        super(String.format("Could not find order with number %d.", orderId));

        this.orderNumber = orderId.toString();
    }
}