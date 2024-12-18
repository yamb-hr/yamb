package com.tejko.yamb.business.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Ticket;

public interface PlayerService extends UserDetailsService {

    Optional<Player> findByExternalId(UUID externalId);

    Player getByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);

    Player getCurrentPlayer();

    Page<Player> getAll(Pageable pageable);

    Page<Player> getAllActive(Pageable pageable);
    
    List<Score> getScoresByPlayerExternalId(UUID playerExternalId);

    List<Game> getGamesByPlayerExternalId(UUID playerExternalId);

    List<Clash> getClashesByPlayerExternalId(UUID playerExternalId);

    List<Log> getLogsByPlayerExternalId(UUID playerExternalId);

    List<Ticket> getTicketsByPlayerExternalId(UUID playerExternalId);

    PlayerStats getPlayerStatsByExternalId(UUID externalId);

    GlobalPlayerStats getGlobalStats();

    PlayerPreferences getPreferencesByPlayerExternalId(UUID playerExternalId);
    
    PlayerPreferences setPreferencesByPlayerExternalId(UUID playerExternalId, PlayerPreferences playerPreferences);

    Player updateUsernameByExternalId(UUID playerExternalId, String username);

    Player updateEmailByExternalId(UUID playerExternalId, String username);

    void deleteInactivePlayers();

    List<PlayerRelationship> getRelationshipsByPlayerExternalId(UUID playerExternalId);

    void mergePlayers(UUID parentExternalId, List<UUID> playerExternalIds);

    Player updateAvatarByExternalId(UUID playerExternalId, MultipartFile avatar);

    List<Notification> getNotificationsByPlayerExternalId(UUID playerExternalId);

    void deleteNotificationsByPlayerExternalId(UUID playerExternalId);
    
}
