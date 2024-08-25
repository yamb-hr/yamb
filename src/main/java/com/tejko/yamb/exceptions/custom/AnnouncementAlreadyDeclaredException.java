package com.tejko.yamb.exceptions.custom;

import com.tejko.yamb.domain.constants.MessageConstants;

public class AnnouncementAlreadyDeclaredException extends IllegalStateException {

    public AnnouncementAlreadyDeclaredException() {
        super(MessageConstants.ERROR_ANNOUNCEMENT_ALREADY_DECLARED);
    }

}
