package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends Exception{

    private final String resource;

    public ItemNotFoundException(Long resource) {
        super(String.format("Item %d not found", resource));

        this.resource = resource.toString();
    }
}
