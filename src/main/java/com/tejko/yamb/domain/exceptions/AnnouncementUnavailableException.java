package com.tejko.yamb.domain.exceptions;

import com.tejko.yamb.domain.constants.MessageConstants;

public class AnnouncementUnavailableException extends IllegalStateException {

    public AnnouncementUnavailableException() {
        super(MessageConstants.ERROR_ANNOUNCEMENT_NOT_AVAILABLE);
    }

}
