package com.tejko.yamb.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.RegisteredPlayer;

@Component
public class AuthContext {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthContext(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Optional<Player> getAuthenticatedPlayer() {
        try {
            return Optional.of((Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public RegisteredPlayer authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (RegisteredPlayer) authentication.getPrincipal();
    }
    
}
