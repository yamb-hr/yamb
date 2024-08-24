package com.tejko.yamb.api.payload;

import java.time.LocalDateTime;
import com.tejko.yamb.domain.enums.MessageType;

public class WebSocketMessage {

    private Long senderId;
    private Long receiverId;
    private MessageType type;
    private Object content;
    private LocalDateTime time;

    public WebSocketMessage(Long senderId, Long receiverId, MessageType type, Object content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.type = type;
        this.content = content;
        this.time = LocalDateTime.now();
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
        return type + " [" + time + "] " + senderId + ": " + content + " -> " + receiverId;
    }

}
