package com.tejko.yamb.domain.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tejko.yamb.domain.enums.LogLevel;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Log extends BaseEntity {

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
        return "[" + createdAt + "]: Message=" + message + "\nPlayer=" + player.getUsername();
    }

}
