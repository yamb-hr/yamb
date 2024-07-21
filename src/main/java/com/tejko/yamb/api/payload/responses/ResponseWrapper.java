package com.tejko.yamb.api.payload.responses;

import com.tejko.yamb.domain.enums.ResponseStatus;

public class ResponseWrapper {

    private String message;
    private Object data;
    private ResponseStatus status;

    public ResponseWrapper(String message, Object data, ResponseStatus status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

}
