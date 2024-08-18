package com.tejko.yamb.api.payload.requests;

public class AuthRequest {

    private String username;
    private String password;

    private AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
}
