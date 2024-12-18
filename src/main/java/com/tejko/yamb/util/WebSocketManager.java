package com.tejko.yamb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.WebSocketMessage;

@Component
public class WebSocketManager {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSocketManager(SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(String destination, WebSocketMessage message) {
        simpMessagingTemplate.send(destination, message);
    }

    public void convertAndSend(String destination, Object content, MessageType type) {
        WebSocketMessage message = WebSocketMessage.getInstance(generatePayload(content), type);
        simpMessagingTemplate.convertAndSend(destination, message, message.getHeaders());
    }

    public void convertAndSendToUser(WebSocketMessage message) {
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getReceiverId()), "/player/private", message, message.getHeaders());
    }

    private byte[] generatePayload(Object content) {
        if (content == null) {
            System.err.println("Warning: Content is null in WebSocketMessage");
            return new byte[0];
        }    
        try {
            return objectMapper.writeValueAsBytes(content);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}