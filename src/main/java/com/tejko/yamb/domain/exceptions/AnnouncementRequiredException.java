package com.tejko.yamb.domain.exceptions;

public class AnnouncementRequiredException extends IllegalGameStateException {

    public AnnouncementRequiredException(Object... args) {
        super("error.announcement_required", args);
    }
    
}
