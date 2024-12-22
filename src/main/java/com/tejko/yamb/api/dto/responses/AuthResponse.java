package com.tejko.yamb.api.dto.responses;

import org.springframework.hateoas.RepresentationModel;

public class AuthResponse extends RepresentationModel<AuthResponse> {

    private PlayerDetailResponse player;
    private String token;

    public AuthResponse() {}

    public AuthResponse(PlayerDetailResponse player, String token) {
        this.player = player;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PlayerDetailResponse getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDetailResponse player) {
        this.player = player;
    }
    
}
