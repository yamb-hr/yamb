package com.tejko.yamb.api.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordChangeRequest {
    
    private String oldPassword;
    
    @NotBlank(message = "error.password_required")
    @Size(min = 6, max = 30, message = "error.password_length_invalid")
    private String newPassword;

    public PasswordChangeRequest() {}

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
}
