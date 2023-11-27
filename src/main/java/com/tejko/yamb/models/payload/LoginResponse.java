package com.tejko.yamb.models.payload;

public class LoginResponse {

    public Long id;
    public String username;
    public String token;

    public LoginResponse(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }
    
}
