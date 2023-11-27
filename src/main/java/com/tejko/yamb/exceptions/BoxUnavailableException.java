package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class BoxUnavailableException extends IllegalStateException {

    public BoxUnavailableException() {
        super(MessageConstants.ERROR_BOX_NOT_AVAILABLE);
    }
    
}
