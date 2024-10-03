package com.tejko.yamb.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tejko.yamb.domain.models.Player;

public class AuthContext {

    public static Player getAuthenticatedPlayer() {
        if (isAuthenticated()) {
            return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        throw new BadCredentialsException("test");
    }

    private static boolean isAuthenticated() {
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser");
    }

}
