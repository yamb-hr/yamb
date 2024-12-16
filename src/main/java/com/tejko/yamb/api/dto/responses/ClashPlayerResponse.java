package com.tejko.yamb.api.dto.responses;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

import com.tejko.yamb.domain.enums.InvitationStatus;

public class ClashPlayerResponse extends RepresentationModel<ClashPlayerResponse> {
 
    private UUID id;
    private UUID gameId;
    private InvitationStatus status;
    
    public ClashPlayerResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }
    
}


