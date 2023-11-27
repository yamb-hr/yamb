package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class AnnouncementRequiredException extends IllegalStateException {

    public AnnouncementRequiredException() {
        super(MessageConstants.ERROR_ANNOUNCEMENT_REQUIRED);
    }
    
}
