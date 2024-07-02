package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAuthorization extends RuntimeException {
    public InvalidAuthorization(String message) {
        super(message);
    }
}