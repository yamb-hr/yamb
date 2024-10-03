package com.tejko.yamb.domain.models;

public class PlayerWithToken {

    private Player player;
    private String token;

    public PlayerWithToken() {}

    public PlayerWithToken(Player player, String token) {
        this.player = player;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
