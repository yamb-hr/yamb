package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AuthRequest {

    @Email(message = "error.invalid_email")
    private String email;

    @Size(min = 3, max = 15, message = "error.username_length_invalid")
    private String username;

    @NotBlank(message = "error.password_required")
    @Size(min = 6, max = 30, message = "error.password_length_invalid")
    private String password;

    public AuthRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public boolean isValid() {
        return (email != null && !email.isEmpty()) || (username != null && !username.isEmpty());
    }

}
