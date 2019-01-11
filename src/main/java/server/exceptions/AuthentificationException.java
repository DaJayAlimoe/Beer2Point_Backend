package server.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Value
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthentificationException extends Exception {

    private final String token;

    public AuthentificationException(String token) {
        super(String.format("Token %s does not exist in database", token));

        this.token = token;
    }
}
