package com.tejko.yamb.api.dto.requests;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class PlayerMergeRequest {

    @NotBlank
    private UUID parentId;
    
    @NotEmpty
    private List<UUID> playerIds;

    public PlayerMergeRequest() {}

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<UUID> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<UUID> playerIds) {
        this.playerIds = playerIds;
    }
    
}
