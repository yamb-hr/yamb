package com.tejko.yamb.exceptions.custom;

public class GameLockedException extends ResourceLockedException {

    public GameLockedException(Object... args) {
        super("error.game_locked", args);
    }

}
