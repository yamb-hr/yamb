package com.tejko.yamb.api.dto.requests;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tejko.yamb.domain.enums.ClashType;

public class ClashRequest {
    
    @NotEmpty(message = "error.player_ids_required")
    private List<UUID> playerIds;

    @NotNull(message = "error.type_required")
    private ClashType type;

    public ClashRequest() {}

    public List<UUID> getPlayerIds() {
        return playerIds;
    }
    public void setPlayerIds(List<UUID> playerIds) {
        this.playerIds = playerIds;
    }
    public ClashType getType() {
        return type;
    }
    public void setType(ClashType type) {
        this.type = type;
    }

}
