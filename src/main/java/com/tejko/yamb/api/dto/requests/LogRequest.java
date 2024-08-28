package com.tejko.yamb.api.dto.requests;

import java.lang.System.Logger.Level;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LogRequest {

    @NotBlank(message = "{error.message_required}")
    private String message;

    private Object data;

    @NotNull(message = "{error.level_required}")
    private Level level;
    
    public LogRequest() {}

    public LogRequest(String message, Object data, Level level) {
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    
}
