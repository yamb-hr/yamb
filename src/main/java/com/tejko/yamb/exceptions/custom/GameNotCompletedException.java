package com.tejko.yamb.exceptions.custom;

public class GameNotCompletedException extends GameStateException {

    public GameNotCompletedException(Object... args) {
        super("error.game_not_completed", args);
    }

}
