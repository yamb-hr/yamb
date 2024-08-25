package com.tejko.yamb.exceptions.custom;

import com.tejko.yamb.domain.constants.MessageConstants;

public class AnnouncementRequiredException extends IllegalStateException {

    public AnnouncementRequiredException() {
        super(MessageConstants.ERROR_ANNOUNCEMENT_REQUIRED);
    }
    
}
