package com.tejko.yamb.api.events;

import com.tejko.yamb.domain.models.Game;

public class GameUpdatedEvent {

    private final Game game;

    public GameUpdatedEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
    
}
