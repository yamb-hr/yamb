package com.tejko.yamb.exceptions.custom;

public class RollRequiredException extends IllegalGameStateException {

    public RollRequiredException(Object... args) {
        super("error.roll_required", args);
    }

}
