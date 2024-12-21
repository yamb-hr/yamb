package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.tejko.yamb.domain.enums.NotificationType;

@Relation(collectionRelation = "notifications")
public class NotificationResponse extends RepresentationModel<NotificationResponse> {

    private UUID id;
    private LocalDateTime createdAt;
    private String content;
    private String link;
    private NotificationType type;

    public NotificationResponse() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
