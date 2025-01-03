package com.tejko.yamb.api.events;

import com.tejko.yamb.domain.models.Player;

public class PlayerEmailUpdatedEvent {
    
    private final Player player;

    public PlayerEmailUpdatedEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
    
}
