package com.tejko.models.payload;

public class ErrorResponse {

    public String error;
    public String message;
    public String detail;

    public ErrorResponse(String error, String message, String detail) {
        this.error = error;
        this.message = message;
        this.detail = detail;
    }

}
