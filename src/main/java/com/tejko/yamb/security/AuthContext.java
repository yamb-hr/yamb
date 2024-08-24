package com.tejko.yamb.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthContext(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Optional<Long> getAuthenticatedPlayerId() {
        try {
            return Optional.of(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Long authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return Long.valueOf(authentication.getName());
    }
    
}
