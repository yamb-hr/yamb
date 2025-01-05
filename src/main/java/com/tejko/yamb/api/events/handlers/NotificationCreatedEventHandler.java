package com.tejko.yamb.api.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.assemblers.NotificationModelAssembler;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.api.events.NotificationCreatedEvent;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.util.WebSocketManager;

@Component
public class NotificationCreatedEventHandler {

    private final WebSocketManager webSocketManager;
    private final NotificationModelAssembler notificationModelAssembler;

    @Autowired
    public NotificationCreatedEventHandler(WebSocketManager webSocketManager, NotificationModelAssembler notificationModelAssembler) {
        this.webSocketManager = webSocketManager;
        this.notificationModelAssembler = notificationModelAssembler;
    }

    @EventListener
    public void handleNotificationCreated(NotificationCreatedEvent event) {
        NotificationResponse notificationResponse = notificationModelAssembler.toModel(event.getNotification());
        webSocketManager.convertAndSendToUser(
            event.getNotification().getPlayer().getExternalId(), 
            notificationResponse, 
            MessageType.NOTIFICATION
        );
    }
    
}
