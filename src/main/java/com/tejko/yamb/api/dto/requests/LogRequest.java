package com.tejko.yamb.api.dto.requests;

public class LogRequest {

    private String message;
    private Object data;
    private String level;
    
    public LogRequest() {}

    public LogRequest(String message, Object data, String level) {
        this.message = message;
        this.data = data;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    
}
