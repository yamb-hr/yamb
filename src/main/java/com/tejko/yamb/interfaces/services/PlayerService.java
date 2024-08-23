package com.tejko.yamb.interfaces.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tejko.yamb.api.payload.responses.PlayerStatsResponse;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.interfaces.BaseService;

public interface PlayerService extends UserDetailsService, BaseService<Player> {
    
    
    public List<Score> getScoresByPlayerId(UUID externalId);

    public Player loadUserByUsername(String username);

    public String getPrincipalByExternalId(UUID externalId);

    public PlayerStatsResponse getPlayerStats(UUID externalId);
}
