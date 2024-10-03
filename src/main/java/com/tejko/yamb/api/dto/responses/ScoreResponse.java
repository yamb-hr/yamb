package com.tejko.yamb.api.dto.responses;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;


@Relation(collectionRelation = "scores")
public class ScoreResponse extends RepresentationModel<ScoreResponse> {

    private UUID id;
    private LocalDateTime createdAt;
    private PlayerResponse player;
    private int value;

    public ScoreResponse() {}

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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
