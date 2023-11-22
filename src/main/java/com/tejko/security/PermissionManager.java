package com.tejko.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tejko.constants.GameConstants;
import com.tejko.models.Player;
import com.tejko.repositories.GameRepository;
import com.tejko.repositories.PlayerRepository;

@Component
public class PermissionManager {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PlayerRepository playerRepo;

    public boolean hasPermission(Authentication authentication, Long gameId) {
        Player player = playerRepo.findByUsername(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_PLAYER_NOT_FOUND));
        return player.getId().equals(gameRepo.findById(gameId).get().getPlayer().getId());
    }

}
