package com.tejko.yamb.domain.exceptions;

public class UndoClashGameException extends IllegalGameStateException {

    public UndoClashGameException(Object... args) {
        super("error.undo_clash_game", args);
    }
    
    
}
