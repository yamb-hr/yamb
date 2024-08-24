package com.tejko.yamb.api.payload.responses;

import java.time.LocalDateTime;

public class ScoreResponse {

    private Long id;
    private LocalDateTime createdAt;
    private PlayerResponse player;
    private int value;

    public ScoreResponse() {}

    public ScoreResponse(Long id, LocalDateTime createdAt, PlayerResponse player, int value) {
        this.id = id;
        this.createdAt = createdAt;
        this.player = player;
        this.value = value;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
