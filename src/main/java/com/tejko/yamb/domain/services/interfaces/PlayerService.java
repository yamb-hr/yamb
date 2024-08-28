package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStats;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStats;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.domain.models.Player;

public interface PlayerService extends UserDetailsService {

    public Player fetchById(Long id);

    public PlayerResponse getById(Long id);

    public List<PlayerResponse> getAll();
    
    public List<ScoreResponse> getScoresByPlayerId(Long id);

    public PlayerStats getPlayerStats(Long id);

    public GlobalPlayerStats getGlobalStats();

    public PlayerPreferencesResponse getPreferencesByPlayerId(Long playerId);
    
    public PlayerPreferencesResponse setPreferencesByPlayerId(Long playerId, PlayerPreferencesRequest playerPreferencesRequest);

    public void deleteInactivePlayers();
    
}
