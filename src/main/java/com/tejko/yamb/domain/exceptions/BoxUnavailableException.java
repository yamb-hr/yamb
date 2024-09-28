package com.tejko.yamb.domain.exceptions;

public class BoxUnavailableException extends IllegalGameStateException {

    public BoxUnavailableException(Object... args) {
        super("error.box_unavailable", args);
    }
    
}
