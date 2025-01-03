package com.tejko.yamb.api.dto.requests;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;

public class PlayerIdSetRequest {
       
    @NotEmpty(message = "error.player_ids_required")
    private Set<UUID> playerIds;

    public PlayerIdSetRequest() {}

    public Set<UUID> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(Set<UUID> playerIds) {
        this.playerIds = playerIds;
    }

}
