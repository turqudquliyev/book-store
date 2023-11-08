package az.ingress.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static az.ingress.model.constant.ExceptionConstant.UNEXPECTED_EXCEPTION;
import static az.ingress.model.constant.ExceptionConstant.VALIDATION_EXCEPTION;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handle(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return ExceptionResponse.builder()
                .code(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handle(AlreadyExistsException ex) {
        log.error("AlreadyExistsException: ", ex);
        return ExceptionResponse.builder()
                .code(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handle(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: ", ex);
        List<ValidationException> exceptions = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationException(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return ExceptionResponse.builder()
                .code(VALIDATION_EXCEPTION)
                .validationExceptions(exceptions)
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handle(BadCredentialsException ex) {
        log.error("BadCredentialsException: ", ex);
        return ExceptionResponse.builder()
                .code(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ExceptionResponse handle(AccessDeniedException ex) {
        log.error("AccessDeniedException: ", ex);
        return ExceptionResponse.builder()
                .code(ex.getMessage())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handle(AuthenticationException ex) {
        log.error("AuthException: ", ex);
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ExceptionResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponse handle(Exception ex) {
        log.error("UnexpectedException: ", ex);
        return ExceptionResponse.builder()
                .code(UNEXPECTED_EXCEPTION)
                .build();
    }
}