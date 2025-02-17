package com.tejko.yamb.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tejko.yamb.domain.models.Player;

public class AuthContext {

    public static Player getAuthenticatedPlayer() {
        try {
            return (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadCredentialsException("error.unauthorized");
        }
    }

}
