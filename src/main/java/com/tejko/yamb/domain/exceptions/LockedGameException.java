package com.tejko.yamb.domain.exceptions;

import com.tejko.yamb.domain.constants.MessageConstants;

public class LockedGameException extends IllegalStateException {

    public LockedGameException() {
        super(MessageConstants.ERROR_LOCKED_GAME);
    }

}
