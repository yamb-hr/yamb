package com.tejko.yamb.models.payload;

import java.util.Set;

import com.tejko.yamb.models.Role;

public class AuthResult {

    public Long id;
    public String username;
    public boolean tempUser;
    public String token;
    public Set<Role> roles;

    public AuthResult(Long id, String username, String token, boolean tempUser, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.tempUser = tempUser;
        this.roles = roles;
    }
    
}
