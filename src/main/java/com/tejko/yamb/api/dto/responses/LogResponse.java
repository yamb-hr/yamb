package com.tejko.yamb.api.dto.responses;

import java.util.UUID;

import java.time.LocalDateTime;

import org.slf4j.event.Level;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "logs")
public class LogResponse extends RepresentationModel<LogResponse> {

    private UUID id;
    private LocalDateTime createdAt;
    private PlayerResponse player;
    private String message;
    private Level level;

    public LogResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
