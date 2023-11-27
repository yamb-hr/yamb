package com.tejko.yamb.exceptions;

import com.tejko.yamb.constants.MessageConstants;

public class AnnouncementAlreadyDeclaredException extends IllegalStateException {

    public AnnouncementAlreadyDeclaredException() {
        super(MessageConstants.ERROR_ANNOUNCEMENT_ALREADY_DECLARED);
    }

}
