package com.tejko.yamb.api.dto.requests;

public class GameRequest {

    private Long playerId;

    public GameRequest() {}

    public GameRequest(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
    
}
