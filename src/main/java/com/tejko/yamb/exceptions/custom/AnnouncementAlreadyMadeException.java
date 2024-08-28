package com.tejko.yamb.exceptions.custom;

public class AnnouncementAlreadyMadeException extends GameStateException {

    public AnnouncementAlreadyMadeException(Object... args) {
        super("error.announcement_already_made", args);
    }

}
