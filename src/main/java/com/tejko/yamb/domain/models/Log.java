package com.tejko.yamb.domain.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    private String level;
    
    private Log() {}

    private Log(Player player, String message, Object data, String level) {
        this.player = player;
        this.message = message;
        this.data = data;
        this.level = level;
    }

    public static Log getInstance(Player player, String message, Object data, String level) {
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

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return player.getUsername() + " [" + createdAt + "]: " + message;
    }

}
