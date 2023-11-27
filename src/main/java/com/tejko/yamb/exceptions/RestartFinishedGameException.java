package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class RestartFinishedGameException extends IllegalStateException {

    public RestartFinishedGameException() {
        super(MessageConstants.ERROR_RESTART_COMPLETED_SHEET);
    }

}
