package com.tejko.yamb.api.dto.responses;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.tejko.yamb.domain.enums.RelationshipType;

public class RelationshipResponse extends RepresentationModel<RelationshipResponse> {

    private UUID id;
    private PlayerResponse player;
    private PlayerResponse relatedPlayer;
    private RelationshipType type;
    private boolean active;

    public RelationshipResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
    }

    public PlayerResponse getRelatedPlayer() {
        return relatedPlayer;
    }

    public void setRelatedPlayer(PlayerResponse relatedPlayer) {
        this.relatedPlayer = relatedPlayer;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }    
    
}
