package com.tejko.yamb.domain.exceptions;

import com.tejko.yamb.domain.constants.MessageConstants;

public class RestartFinishedGameException extends IllegalStateException {

    public RestartFinishedGameException() {
        super(MessageConstants.ERROR_RESTART_COMPLETED_SHEET);
    }

}
