package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tejko.yamb.domain.enums.LogLevel;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity(name = "log")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Log {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Type(type = "jsonb")
    @Column(name= "data" , columnDefinition = "jsonb")
    private Object data;

    @Column(name = "message")
    private String message;
    
    @Column(name = "level")
    private LogLevel level;

    private Log() {}

    private Log(Player player, String message, LogLevel level, Object data) {
        this.player = player;
        this.message = message;
        this.level = level;
        this.data = data;
    }

    public static Log getInstance(Player player, String message, LogLevel level, Object data) {
        return new Log(player, message, level, data);
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

    public LogLevel getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return player.getUsername() + " [" + getCreatedAt() + "]: " + message;
    }

}
