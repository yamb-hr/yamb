package com.tejko.yamb.domain.exceptions;

public class GameLockedException extends ResourceLockedException {

    public GameLockedException(Object... args) {
        super("error.game_locked", args);
    }

}
