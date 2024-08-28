package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;
import java.lang.System.Logger.Level;

public class LogResponse  {

    private Long id;
    private LocalDateTime createdAt;
    private PlayerResponse player;
    private Object data;
    private String message;
    private Level level;

    public LogResponse() {}

    public LogResponse(Long id, LocalDateTime createdAt, PlayerResponse player, Object data, String message, Level level) {
        this.id = id;
        this.createdAt = createdAt;
        this.player = player;
        this.data = data;
        this.message = message;
        this.level = level;
    }

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

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
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
