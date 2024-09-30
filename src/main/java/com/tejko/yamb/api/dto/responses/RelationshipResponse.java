package com.tejko.yamb.api.dto.responses;

import com.tejko.yamb.domain.enums.RelationshipType;
import com.tejko.yamb.domain.models.Player;

public class RelationshipResponse {

    private Player player;
    
    private RelationshipType type;

    public RelationshipResponse() {}

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }
    
}
