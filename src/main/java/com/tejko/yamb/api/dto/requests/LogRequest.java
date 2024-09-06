package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ch.qos.logback.classic.Level;

public class LogRequest {

    @NotBlank(message = "error.message_required")
    private String message;

    private Object data;

    @NotNull(message = "error.level_required")
    private Level level;
    
    public LogRequest() {}
    
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    
}
