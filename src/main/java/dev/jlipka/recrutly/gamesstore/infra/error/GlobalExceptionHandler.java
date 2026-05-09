package dev.jlipka.recrutly.gamesstore.infra.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
