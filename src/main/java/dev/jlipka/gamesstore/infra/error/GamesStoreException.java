package dev.jlipka.gamesstore.infra.error;

public class GamesStoreException extends RuntimeException {

    private final ErrorCode errorCode;
    public GamesStoreException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
