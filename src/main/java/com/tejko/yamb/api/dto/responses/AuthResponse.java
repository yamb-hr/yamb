package com.tejko.yamb.api.dto.responses;

public class AuthResponse {

    private PlayerResponse player;
    private String token;

    public AuthResponse() {}

    public AuthResponse(PlayerResponse player, String token) {
        this.player = player;
        this.token = token;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerResponse player) {
        this.player = player;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
}
