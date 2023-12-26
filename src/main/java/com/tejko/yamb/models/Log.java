package com.tejko.yamb.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tejko.yamb.models.enums.LogLevel;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Log extends DatabaseEntity {

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Object data;

    @Column
    private String message;
    
    @Column
    private LogLevel level;

    @Column
    private LocalDateTime time;

    private Log() {}

    private Log(Player player, String message, LogLevel level, Object data, LocalDateTime time) {
        this.player = player;
        this.message = message;
        this.level = level;
        this.data = data;
        this.time = time;
    }

    public static Log getInstance(Player player, String message, LogLevel level, Object data) {
        return new Log(player, message, level, data, LocalDateTime.now());
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

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "[" + time + "]: Message=" + message + "\nPlayer=" + player.getUsername();
    }

}
