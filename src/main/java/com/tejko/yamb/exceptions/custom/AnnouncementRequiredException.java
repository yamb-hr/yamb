package com.tejko.yamb.exceptions.custom;

public class AnnouncementRequiredException extends GameStateException {

    public AnnouncementRequiredException(Object... args) {
        super("error.announcement_required", args);
    }
    
}
