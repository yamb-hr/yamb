package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotNull;

public class GameRequest {

    @NotNull(message = "error.player_id_required")
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
