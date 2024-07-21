package com.tejko.yamb.api.payload.responses;

import java.util.Set;
import java.util.UUID;

import com.tejko.yamb.domain.models.Role;

public class AuthResponse {

    public UUID id;
    public String username;
    public boolean tempUser;
    public String token;
    public Set<Role> roles;

    public AuthResponse(UUID id, String username, String token, boolean tempUser, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.tempUser = tempUser;
        this.roles = roles;
    }
    
}
