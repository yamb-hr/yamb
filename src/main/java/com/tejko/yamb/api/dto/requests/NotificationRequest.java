package com.tejko.yamb.api.dto.requests;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.tejko.yamb.domain.enums.NotificationType;

public class NotificationRequest {

    @NotNull(message = "error.player_id_required")
    private UUID playerId;
    
    @NotNull(message = "error.content_id_required")
    private String content;

    @NotNull(message = "error.link_id_required")
    private String link;
    
    @NotNull(message = "error.type_id_required")
    private NotificationType type;

    public NotificationRequest() {}

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
    
}
