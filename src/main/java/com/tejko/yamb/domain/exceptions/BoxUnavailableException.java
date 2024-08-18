package com.tejko.yamb.domain.exceptions;

import com.tejko.yamb.domain.constants.MessageConstants;

public class BoxUnavailableException extends IllegalStateException {

    public BoxUnavailableException() {
        super(MessageConstants.ERROR_BOX_NOT_AVAILABLE);
    }
    
}
