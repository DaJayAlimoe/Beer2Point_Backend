package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{

    private final String resource;

    public ResourceNotFoundException(String resource) {
        super(String.format("Resource %s not found", resource));

        this.resource = resource;
    }
}
