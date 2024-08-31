package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.Score;

public interface PlayerService extends UserDetailsService {

    public Player getById(Long id);

    public List<Player> getAll();
    
    public List<Score> getScoresByPlayerId(Long id);

    public PlayerStats getPlayerStats(Long id);

    public GlobalPlayerStats getGlobalStats();

    public PlayerPreferences getPreferencesByPlayerId(Long playerId);
    
    public PlayerPreferences setPreferencesByPlayerId(Long playerId, PlayerPreferences playerPreferences);

    public void deleteInactivePlayers();
    
}
