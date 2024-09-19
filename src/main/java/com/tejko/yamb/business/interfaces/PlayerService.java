package com.tejko.yamb.business.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.Score;

public interface PlayerService extends UserDetailsService {

    public Player getByExternalId(UUID externalId);

    public Player getCurrentPlayer();

    public Page<Player> getAll(Pageable pageable);
    
    public List<Score> getScoresByPlayerExternalId(UUID playerExternalId);

    public PlayerStats getPlayerStatsByExternalId(UUID externalId);

    public GlobalPlayerStats getGlobalStats();

    public PlayerPreferences getPreferencesByPlayerExternalId(UUID playerExternalId);
    
    public PlayerPreferences setPreferencesByPlayerExternalId(UUID playerExternalId, PlayerPreferences playerPreferences);

    public Player changeUsernameByExternalId(UUID playerExternalId, String username);

    public void deleteInactivePlayers();
    
}
