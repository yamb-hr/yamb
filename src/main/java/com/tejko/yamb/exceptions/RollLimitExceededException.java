package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class RollLimitExceededException extends IllegalStateException {

    public RollLimitExceededException() {
        super(MessageConstants.ERROR_ROLL_LIMIT_EXCEEDED);
    }
    
}
