package com.tejko.yamb.exceptions.custom;

public class BoxUnavailableException extends IllegalGameStateException {

    public BoxUnavailableException(Object... args) {
        super("error.box_unavailable", args);
    }
    
}
