package com.tejko.yamb.api.dto.responses;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.tejko.yamb.domain.enums.RelationshipType;
import com.tejko.yamb.domain.models.Player;

public class RelationshipResponse extends RepresentationModel<RelationshipResponse> {

    private UUID id;
    private Player player;
    private Player relatedPlayer;
    private RelationshipType type;
    private boolean active;

    public RelationshipResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getRelatedPlayer() {
        return relatedPlayer;
    }

    public void setRelatedPlayer(Player relatedPlayer) {
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
