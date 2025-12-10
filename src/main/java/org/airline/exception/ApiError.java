package org.airline.exception;

public class ApiError {
    private final int status;
    private final String code;
    private final String message;

    public ApiError(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}
