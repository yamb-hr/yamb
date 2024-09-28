package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.domain.enums.MessageType;

public class WebSocketMessage implements Message<byte[]> {

    private ObjectMapper objectMapper;
    private UUID senderId;
    private UUID receiverId;
    private Object content;
    private MessageType type;
    private final LocalDateTime timestamp = LocalDateTime.now();
    private MessageHeaders headers;

    public WebSocketMessage(ObjectMapper objectMapper, MessageType type, Object content) {
        this.objectMapper = objectMapper;
        this.type = type;
        this.content = content;
        this.headers = createHeaders();
    }

    public WebSocketMessage(ObjectMapper objectMapper, UUID senderId, UUID receiverId, MessageType type, Object content) {
        this.objectMapper = objectMapper;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.headers = createHeaders();
    }

    private MessageHeaders createHeaders() {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("senderId", senderId);
        headerMap.put("receiverId", receiverId);
        headerMap.put("messageType", type);
        headerMap.put("timestamp", timestamp);

        return new MessageHeaders(headerMap);
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public UUID getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public byte[] getPayload() {
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

    @Override
    public MessageHeaders getHeaders() {
        return headers;
    }
    
}
