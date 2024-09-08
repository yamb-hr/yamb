package com.tejko.yamb.domain.models.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.slf4j.event.Level;

@Entity(name = "log")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // player is null for system logs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;

    @Type(type = "jsonb")
    @Column(name = "data", nullable = true, columnDefinition = "jsonb")
    private Object data;

    // set type to text
    @Column(name = "message", nullable = false, columnDefinition = "text")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;
    
    protected Log() {}

    protected Log(Player player, String message, Object data, Level level) {
        this.player = player;
        this.message = message;
        this.data = data;
        this.level = level;
    }

    public static Log getInstance(Player player, String message, Object data, Level level) {
        return new Log(player, message, data, level);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Player getPlayer() {
        return player;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return player.getUsername() + " [" + createdAt + "]: " + message;
    }

}
