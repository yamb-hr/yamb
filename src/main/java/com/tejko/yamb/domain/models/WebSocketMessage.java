package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;

import com.tejko.yamb.domain.enums.MessageType;

public class WebSocketMessage {

    private Long senderId;
    private Long receiverId;
    private Object content;
    private MessageType type;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public WebSocketMessage() {}

    public WebSocketMessage(Long senderId, Long receiverId, MessageType type, Object content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
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
