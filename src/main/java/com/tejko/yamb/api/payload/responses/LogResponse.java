package com.tejko.yamb.api.payload.responses;

import com.tejko.yamb.domain.enums.LogLevel;

public class LogResponse extends BaseDTO {

    public String player;
    public String data;
    public String message;
    public LogLevel level;

}
