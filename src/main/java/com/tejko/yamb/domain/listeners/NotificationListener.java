package com.tejko.yamb.domain.listeners;

import javax.persistence.PostPersist;

import com.tejko.yamb.api.events.NotificationCreatedEvent;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.util.ApplicationContextProvider;

public class NotificationListener {

    @PostPersist
    public void onPostPersist(Notification notification) {
        ApplicationContextProvider.publishEvent(new NotificationCreatedEvent(notification));
    }

}