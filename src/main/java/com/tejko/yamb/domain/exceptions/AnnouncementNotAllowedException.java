package com.tejko.yamb.domain.exceptions;

public class AnnouncementNotAllowedException extends IllegalGameStateException {

    public AnnouncementNotAllowedException(Object... args) {
        super("error.announcement_not_allowed", args);
    }

}
