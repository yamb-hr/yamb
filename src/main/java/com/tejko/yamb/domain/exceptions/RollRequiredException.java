package com.tejko.yamb.domain.exceptions;

public class RollRequiredException extends IllegalGameStateException {

    public RollRequiredException(Object... args) {
        super("error.roll_required", args);
    }

}
