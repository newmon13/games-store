package dev.jlipka.recrutly.gamesstore.infra.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    GAME_NOT_FOUND(1001, HttpStatus.NOT_FOUND),
    GAME_ALREADY_EXISTS(1002, HttpStatus.CONFLICT),
    INVALID_DISCOUNTED_PRICE(1003, HttpStatus.BAD_REQUEST)

    ;

    private final int code;
    private final HttpStatus status;

    ErrorCode(int code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}