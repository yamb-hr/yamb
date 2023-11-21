package com.tejko.components;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tejko.repositories.GameRepository;
import com.tejko.repositories.PlayerRepository;

@Component
public class PermissionComponent {

    @Autowired
    PlayerRepository playerRepository;
    
    @Autowired
    GameRepository gameRepository;

    public boolean hasPermission(UUID id) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return true;
    }
}