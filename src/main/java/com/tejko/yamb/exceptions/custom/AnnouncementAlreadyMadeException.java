package com.tejko.yamb.exceptions.custom;

public class AnnouncementAlreadyMadeException extends IllegalGameStateException {

    public AnnouncementAlreadyMadeException(Object... args) {
        super("error.announcement_already_made", args);
    }

}
