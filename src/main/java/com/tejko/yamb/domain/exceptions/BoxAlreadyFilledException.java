package com.tejko.yamb.domain.exceptions;

public class BoxAlreadyFilledException extends IllegalGameStateException {

    public BoxAlreadyFilledException(Object... args) {
        super("error.box_already_filled", args);
    }
    
}
