package com.tejko.yamb.models.payload;

import com.tejko.yamb.models.enums.ResponseStatus;

public class RestResponse {

    private String message;
    private Object data;
    private ResponseStatus status;

    public RestResponse(String message, Object data, ResponseStatus status) {
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
