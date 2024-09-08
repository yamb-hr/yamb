package com.tejko.yamb.business.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.entities.Player;
import com.tejko.yamb.domain.models.entities.PlayerPreferences;
import com.tejko.yamb.domain.models.entities.Score;

public interface PlayerService extends UserDetailsService {

    public Player getById(Long id);

    public List<Player> getAll();
    
    public List<Score> getScoresByPlayerId(Long id);

    public PlayerStats getPlayerStats(Long id);

    public GlobalPlayerStats getGlobalStats();

    public PlayerPreferences getPreferencesByPlayerId(Long playerId);
    
    public PlayerPreferences setPreferencesByPlayerId(Long playerId, PlayerPreferences playerPreferences);

    public void deleteInactivePlayers();

    public Player changeUsername(Long playerId, String username);
    
}
