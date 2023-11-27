package com.tejko.yamb.models.payload;

public class ResponseWrapper {

    private String message;
    private Object data;

    public ResponseWrapper(String message, Object data) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
