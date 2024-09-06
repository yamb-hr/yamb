package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthRequest {

    @NotBlank(message = "error.username_required")
    @Size(min = 5, max = 15, message = "error.username_length_invalid")
    private String username;

    @NotBlank(message = "error.password_required")
    @Size(min = 5, max = 15, message = "error.password_length_invalid")
    private String password;

    public AuthRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
