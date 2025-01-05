package com.tejko.yamb.api.events;

import com.tejko.yamb.domain.models.Notification;

public class NotificationCreatedEvent {

    private final Notification notification;

    public NotificationCreatedEvent(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }
    
}
