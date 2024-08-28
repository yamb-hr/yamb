package com.tejko.yamb.exceptions.custom;

public class AnnouncementNotAllowedException extends GameStateException {

    public AnnouncementNotAllowedException(Object... args) {
        super("error.announcement_not_allowed", args);
    }

}
