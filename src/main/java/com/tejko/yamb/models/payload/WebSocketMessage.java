package com.tejko.yamb.models.payload;

import java.time.LocalDateTime;

import com.tejko.yamb.models.enums.MessageType;

public class WebSocketMessage {

    private String sender;
    private String receiver;
    private MessageType type;
    private Object content;
    private LocalDateTime time;

    public WebSocketMessage(String sender, String receiver, MessageType type, Object content) {
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.content = content;
        this.time = LocalDateTime.now();
    }
    
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public MessageType getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return type + " [" + time + "] " + sender + ": " + content + " -> " + receiver;
    }

}