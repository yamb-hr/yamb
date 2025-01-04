package com.tejko.yamb.util;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.WebSocketMessage;

@Component
public class WebSocketManager {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketManager(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void send(String destination, WebSocketMessage message) {
        simpMessagingTemplate.send(destination, message);
    }

    public void sendToUser(WebSocketMessage message) {        
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getReceiverId()), "/private", message);
    }

    public void convertAndSend(String destination, Object content, MessageType type) {
        WebSocketMessage message = WebSocketMessage.getInstance(content, type);
        simpMessagingTemplate.convertAndSend(destination, message, message.getHeaders());
    }

    public void convertAndSendToUser(UUID playerExternalId, Object content, MessageType type) {
        WebSocketMessage message = WebSocketMessage.getInstance(content, type);
        simpMessagingTemplate.convertAndSendToUser(
            String.valueOf(playerExternalId),
            "/private",
            message,
            message.getHeaders()
        );
    }

}