package com.tejko.yamb.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.GameRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;

@Component
public class PermissionManager {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PlayerRepository playerRepo;

    public boolean hasGamePermission(Authentication authentication, UUID gameExternalId) {
        Player player = playerRepo.findByUsername(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return player.getId().equals(gameRepo.findByExternalId(gameExternalId).get().getPlayer().getId());
    }

    public boolean hasPlayerPermission(Authentication authentication, UUID playerExternalId) {
        Player player = playerRepo.findByUsername(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return player.getId().equals(playerExternalId);
    }


}
