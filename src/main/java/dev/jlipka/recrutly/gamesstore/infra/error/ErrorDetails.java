package dev.jlipka.recrutly.gamesstore.infra.error;

public class ErrorDetails {
    private int code;
    private String message;
    private String cause;
    private String action;

    public ErrorDetails(int code, String message, String cause, String action) {
        this.code = code;
        this.message = message;
        this.cause = cause;
        this.action = action;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}