package com.tejko.yamb.models.payload;

public class PlayerCredentials {

    private String username;
    private String password;

    private PlayerCredentials(String username, String password) {
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
