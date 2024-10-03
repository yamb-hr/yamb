package com.tejko.yamb.api.dto.requests;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.tejko.yamb.domain.enums.RelationshipType;

public class RelationshipRequest {

    @NotBlank
    private UUID playerId;

    @NotBlank
    private UUID relatedPlayerId;

    @NotBlank
    private RelationshipType type;

    public RelationshipRequest() {}

    public UUID getPlayerId() {
        return playerId;
    }

    public UUID getRelatedPlayerId() {
        return relatedPlayerId;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

}
