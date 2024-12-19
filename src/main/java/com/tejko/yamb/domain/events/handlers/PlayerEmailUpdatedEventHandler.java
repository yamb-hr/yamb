package com.tejko.yamb.domain.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.events.PlayerEmailUpdatedEvent;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.util.EmailManager;

@Component
public class PlayerEmailUpdatedEventHandler {

    private final EmailManager emailManager;

    @Autowired
    public PlayerEmailUpdatedEventHandler(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    @EventListener
    public void handlePlayerEmailUpdated(PlayerEmailUpdatedEvent event) {
        Player player = event.getPlayer();
        if (!player.isEmailVerified() && player.getEmailVerificationToken() != null) {
            String verificationLink = "https://jamb.com.hr/email-verification?token=" + player.getEmailVerificationToken();
            emailManager.sendVerificationEmail(player.getEmail(), player.getUsername(), verificationLink);
        }
    }
    
}
