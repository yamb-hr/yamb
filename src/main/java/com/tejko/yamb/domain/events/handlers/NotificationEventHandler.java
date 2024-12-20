package com.tejko.yamb.domain.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.events.NotificationCreatedEvent;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.util.WebSocketManager;

@Component
public class NotificationEventHandler {

    private final WebSocketManager webSocketManager;

    @Autowired
    public NotificationEventHandler(WebSocketManager webSocketManager) {
        this.webSocketManager = webSocketManager;
    }

    @EventListener
    public void handleNotificationCreated(NotificationCreatedEvent event) {
        Notification notification = event.getNotification();
        webSocketManager.convertAndSendToUser(
            notification.getPlayer().getExternalId(), 
            notification, 
            MessageType.NOTIFICATION
        );
    }
    
}
