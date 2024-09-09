package com.tejko.yamb.exceptions.custom;

public class BoxAlreadyFilledException extends IllegalGameStateException {

    public BoxAlreadyFilledException(Object... args) {
        super("error.box_already_filled", args);
    }
    
}
