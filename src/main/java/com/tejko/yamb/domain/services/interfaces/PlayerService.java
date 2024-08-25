package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.domain.models.Player;

public interface PlayerService extends UserDetailsService {

    public Player fetchById(Long id);

    public PlayerResponse getById(Long id);

    public List<PlayerResponse> getAll();
    
    public List<ScoreResponse> getScoresByPlayerId(Long id);

    public PlayerStatsResponse getPlayerStats(Long id);
}
