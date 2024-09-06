package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

public class LogResponse  {

    private Long id;
    private LocalDateTime createdAt;
    private ShortPlayerResponse player;
    private Object data;
    private String message;
    private Level level;

    public LogResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ShortPlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(ShortPlayerResponse player) {
        this.player = player;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

}
