package com.example.JavaQuanLyKho.exception;

import java.time.OffsetDateTime;

public class ApiErrorResponse {

    private final OffsetDateTime timestamp;
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String path;

    public ApiErrorResponse(OffsetDateTime timestamp, int status, String error, String code, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
