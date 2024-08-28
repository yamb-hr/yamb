package com.tejko.yamb.exceptions.custom;

public class RollLimitExceededException extends GameStateException {

    public RollLimitExceededException(Object... args) {
        super("error.roll_limit_exceeded", args);
    }
    
    
}
