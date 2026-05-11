package dev.jlipka.gamesstore.infra.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorCodeResolver errorCodeResolver;

    public GlobalExceptionHandler(ErrorCodeResolver errorCodeResolver) {
        this.errorCodeResolver = errorCodeResolver;
    }

    @ExceptionHandler(GamesStoreException.class)
    public ResponseEntity<ErrorDetails> handleAppException(GamesStoreException ex) {
        ErrorDetails details = errorCodeResolver.resolve(ex.getErrorCode());
        return ResponseEntity.status(ex.getErrorCode().getStatus()).body(details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
