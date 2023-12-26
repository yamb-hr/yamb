package com.tejko.yamb.models.payload.dto;

import java.time.LocalDateTime;

public class ScoreDTO {

    private Long id;
	private String player;
	private int value;
	private LocalDateTime date;

    public ScoreDTO(Long id, String player, int value, LocalDateTime date) {
        this.id = id;
        this.player = player;
        this.value = value;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getPlayer() {
        return player;
    }

    public int getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }
    
}
