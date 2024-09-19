package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tejko.yamb.domain.enums.MessageType;

public class WebSocketMessage {

    private UUID senderId;
    private UUID receiverId;
    private Object content;
    private MessageType type;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public WebSocketMessage() {}

    public WebSocketMessage(UUID senderId, UUID receiverId, MessageType type, Object content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
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
    
}
