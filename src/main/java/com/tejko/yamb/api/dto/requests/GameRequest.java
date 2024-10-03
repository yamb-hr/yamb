package com.tejko.yamb.api.dto.requests;

import java.util.UUID;

import javax.validation.constraints.NotNull;

public class GameRequest {

    @NotNull(message = "error.player_id_required")
    private UUID playerId;

    public GameRequest() {}

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }
    
}
