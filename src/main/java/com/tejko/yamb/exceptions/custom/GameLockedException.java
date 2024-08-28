package com.tejko.yamb.exceptions.custom;

public class GameLockedException extends GameStateException {

    public GameLockedException(Object... args) {
        super("error.game_locked", args);
    }

}
