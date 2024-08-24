package com.tejko.yamb.api.payload.requests;

import java.util.UUID;

public class GameRequest {

    private UUID playerId;

    public GameRequest() {}

    public GameRequest(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }
    
}
