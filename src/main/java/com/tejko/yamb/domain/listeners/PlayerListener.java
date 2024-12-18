package com.tejko.yamb.domain.listeners;

import javax.persistence.PostUpdate;

import org.springframework.beans.factory.annotation.Autowired;

import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.util.EmailManager;

public class PlayerListener {

    private final EmailManager emailManager;

    @Autowired
    public PlayerListener(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    @PostUpdate
    public void onEmailUpdate(Player player) {
        if (isEmailUpdated(player)) {
            if (!player.isEmailVerified() && player.getEmailVerificationToken() != null) {
                String verificationLink = "https://jamb.com.hr/email-verification?token=" + player.getEmailVerificationToken();
                emailManager.sendVerificationEmail(player.getEmail(), player.getUsername(), verificationLink);
            }
        }
    }

    public boolean isEmailUpdated(Player player) {
        if (player == null || player.getEmail() == null) {
            return false;
        }
        return !player.getEmail().equals(player.getPreviousEmail());
    }

}
