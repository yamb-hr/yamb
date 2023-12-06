package com.tejko.yamb.models.payload;

public class LoginResponse {

    public Long id;
    public String username;
    public boolean tempUser;
    public String token;

    public LoginResponse(Long id, String username, String token, boolean tempUser) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.tempUser = tempUser;
    }
    
}
