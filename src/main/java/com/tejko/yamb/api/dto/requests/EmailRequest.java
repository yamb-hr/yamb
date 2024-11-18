package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailRequest {
    
    @Email
    @NotBlank(message = "error.email_required")
    private String email;

    public EmailRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
