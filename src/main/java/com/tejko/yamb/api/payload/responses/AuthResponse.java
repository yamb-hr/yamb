package com.tejko.yamb.api.payload.responses;

public class AuthResponse {

    private Long playerId;
    private String token;

    public AuthResponse() {}

    public AuthResponse(Long playerId, String token) {
        this.playerId = playerId;
        this.token = token;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
