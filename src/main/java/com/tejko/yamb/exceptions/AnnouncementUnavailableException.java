package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class AnnouncementUnavailableException extends IllegalStateException {

    public AnnouncementUnavailableException() {
        super(MessageConstants.ERROR_ANNOUNCEMENT_NOT_AVAILABLE);
    }

}
