package com.tejko.yamb.domain.exceptions;

public class RollLimitExceededException extends IllegalGameStateException {

    public RollLimitExceededException(Object... args) {
        super("error.roll_limit_exceeded", args);
    }
    
    
}
