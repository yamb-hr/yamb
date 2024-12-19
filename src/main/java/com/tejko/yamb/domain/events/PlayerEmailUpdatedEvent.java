package com.tejko.yamb.domain.events;

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
