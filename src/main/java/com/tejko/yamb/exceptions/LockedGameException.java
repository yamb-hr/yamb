package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class LockedGameException extends IllegalStateException {

    public LockedGameException() {
        super(MessageConstants.ERROR_LOCKED_GAME);
    }

}
