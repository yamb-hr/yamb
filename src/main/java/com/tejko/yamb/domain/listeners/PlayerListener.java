package com.tejko.yamb.domain.listeners;

import javax.persistence.PostUpdate;

import com.tejko.yamb.domain.events.PlayerEmailUpdatedEvent;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.util.ApplicationContextProvider;

public class PlayerListener {

    @PostUpdate
    public void onEmailUpdate(Player player) {
        if (isEmailUpdated(player) && !player.isEmailVerified() && player.getEmailVerificationToken() != null) {
            ApplicationContextProvider.publishEvent(new PlayerEmailUpdatedEvent(player));
        }
    }

    public boolean isEmailUpdated(Player player) {
        if (player == null || player.getEmail() == null) {
            return false;
        }
        return !player.getEmail().equals(player.getPreviousEmail());
    }
    
}
