package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UsernameRequest {

    @NotBlank(message = "error.username_required")
    @Size(min = 3, max = 18, message = "error.username_length_invalid")
    private String username;

    public UsernameRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
