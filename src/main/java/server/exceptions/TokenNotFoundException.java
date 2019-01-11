package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends Exception{

    private final String resource;

    public TokenNotFoundException(String resource) {
        super(String.format("Token %s not found", resource));

        this.resource = resource;
    }
}
