package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tejko.yamb.domain.enums.NotificationType;
import com.tejko.yamb.domain.listeners.NotificationListener;

@Entity(name = "notification")
@EntityListeners(NotificationListener.class)
@Table(name = "notification", indexes = {
    @Index(name = "idx_notification_external_id", columnList = "external_id")
})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "external_id", nullable = false, updatable = false, unique = true)
    private UUID externalId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @JsonIgnore
    private Player player;

    @Column(name = "content", updatable = false)
    private String content;

    @Column(name = "link", updatable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    protected Notification() {}

    protected Notification(Player player, String content, String link, NotificationType type) {
        this.player = player;
        this.content = content;
        this.link = link;
        this.type = type;
    }

    public static Notification getInstance(Player player, String content, String link, NotificationType type) {
        return new Notification(player, content, link, type);
    }

    public Long getId() {
        return id;
    }

    public UUID getExternalId() {
        return externalId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Player getPlayer() {
        return player;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public NotificationType getType() {
        return type;
    }
    
	@PrePersist
    private void ensureExternalId() {
        if (this.externalId == null) {
            this.externalId = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        return type + " " + player.getName() + " " + content;
    }
    
}
