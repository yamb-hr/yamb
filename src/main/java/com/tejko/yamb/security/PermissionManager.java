package com.tejko.yamb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.repositories.GameRepository;
import com.tejko.yamb.repositories.PlayerRepository;

@Component
public class PermissionManager {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PlayerRepository playerRepo;

    public boolean hasGamePermission(Authentication authentication, Long gameId) {
        Player player = playerRepo.findByUsername(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return player.getId().equals(gameRepo.findById(gameId).get().getPlayer().getId());
    }

    public boolean hasPlayerPermission(Authentication authentication, Long playerId) {
        Player player = playerRepo.findByUsername(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return player.getId().equals(playerId);
    }


}
