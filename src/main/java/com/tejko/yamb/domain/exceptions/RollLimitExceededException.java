package com.tejko.yamb.domain.exceptions;

import com.tejko.yamb.domain.constants.MessageConstants;

public class RollLimitExceededException extends IllegalStateException {

    public RollLimitExceededException() {
        super(MessageConstants.ERROR_ROLL_LIMIT_EXCEEDED);
    }
    
}
