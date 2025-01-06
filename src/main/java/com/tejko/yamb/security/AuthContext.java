package com.tejko.yamb.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.tejko.yamb.domain.models.Player;

public class AuthContext {

    public static Player getAuthenticatedPlayer() {
        if (isAuthenticated()) {
            return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            return null;
        }
    }

    private static boolean isAuthenticated() {
        try {
            return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }

}
