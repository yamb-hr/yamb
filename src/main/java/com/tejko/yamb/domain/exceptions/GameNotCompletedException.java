package com.tejko.yamb.domain.exceptions;

public class GameNotCompletedException extends IllegalGameStateException {

    public GameNotCompletedException(Object... args) {
        super("error.game_not_completed", args);
    }

}
