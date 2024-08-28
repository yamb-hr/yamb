package com.tejko.yamb.exceptions.custom;

public class BoxAlreadyFilledException extends GameStateException {

    public BoxAlreadyFilledException(Object... args) {
        super("error.box_already_filled", args);
    }
    
}
