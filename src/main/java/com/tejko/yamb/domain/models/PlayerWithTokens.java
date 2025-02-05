package com.tejko.yamb.domain.models;

public class PlayerWithTokens {

    private Player player;
    private String accessToken;
    private String refreshToken;

    public PlayerWithTokens(Player player, String accessToken, String refreshToken) {
        this.player = player;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Player getPlayer() {
        return player;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
}
