package com.tejko.yamb.api.dto.responses;

import java.time.LocalDateTime;

public class ShortScoreResponse {

    private Long id;
    private LocalDateTime createdAt;
    private int value;

    public ShortScoreResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
