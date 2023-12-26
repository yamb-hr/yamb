package com.tejko.yamb.models.payload.dto;

import java.time.LocalDateTime;

import com.tejko.yamb.models.enums.LogLevel;

public class LogDTO {
    
    private Long id;
    private String player;
    private String data;
    private String message;
    private LogLevel level;
    private LocalDateTime timestamp;

    public LogDTO(Long id, String player, String data, String message, LogLevel level, LocalDateTime timestamp) {
        this.id = id;
        this.player = player;
        this.data = data;
        this.message = message;
        this.level = level;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLevel() {
        return level;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }    

}
