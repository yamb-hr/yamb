package com.tejko.yamb.security;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;

import com.tejko.yamb.domain.models.entities.Player;

public class AuthContext {

    public static Optional<Player> getAuthenticatedPlayer() {
        try {
            return Optional.of((Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
