package az.ingress.exception;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AuthenticationException extends RuntimeException {
    int httpStatus;

    public AuthenticationException(String code, int httpStatus) {
        super(code);
        this.httpStatus = httpStatus;
    }
}