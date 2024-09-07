package com.tejko.yamb.api.dto.requests;

public class PasswordChangeRequest {
    
    private String oldPassword;
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
