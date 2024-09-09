package com.tejko.yamb.exceptions.custom;

public class RollLimitExceededException extends IllegalGameStateException {

    public RollLimitExceededException(Object... args) {
        super("error.roll_limit_exceeded", args);
    }
    
    
}
