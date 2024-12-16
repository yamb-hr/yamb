package com.tejko.yamb.api.dto.requests;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tejko.yamb.domain.enums.ClashType;

public class ClashRequest {
    
    // @NotNull(message = "error.name_required")
    private String name;

    @NotNull(message = "error.player_ids_required")
    private UUID ownerId;

    @NotEmpty(message = "error.player_ids_required")
    private Set<UUID> playerIds;

    @NotNull(message = "error.type_required")
    private ClashType type;

    public ClashRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public Set<UUID> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(Set<UUID> playerIds) {
        this.playerIds = playerIds;
    }

    public ClashType getType() {
        return type;
    }

    public void setType(ClashType type) {
        this.type = type;
    }

}
