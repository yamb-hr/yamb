package com.tejko.yamb.exceptions.custom;

public class AnnouncementRequiredException extends IllegalGameStateException {

    public AnnouncementRequiredException(Object... args) {
        super("error.announcement_required", args);
    }
    
}
