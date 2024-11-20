package com.tejko.yamb.business.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Ticket;
import com.tejko.yamb.domain.repositories.ClashRepository;
import com.tejko.yamb.domain.repositories.GameRepository;
import com.tejko.yamb.domain.repositories.LogRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RelationshipRepository;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.repositories.TicketRepository;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.EmailManager;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;
    private final GameRepository gameRepo;
    private final ClashRepository clashRepo;
    private final RelationshipRepository relationshipRepo;
    private final LogRepository logRepo;
    private final TicketRepository ticketRepo;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepo, ScoreRepository scoreRepo, 
                            GameRepository gameRepo, ClashRepository clashRepo, 
                            RelationshipRepository relationshipRepo, LogRepository logRepo, 
                            TicketRepository ticketRepo) {
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
        this.gameRepo = gameRepo;
        this.clashRepo = clashRepo;
        this.relationshipRepo = relationshipRepo;
        this.logRepo = logRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public Optional<Player> findByExternalId(UUID externalId) {
        return playerRepo.findByExternalId(externalId);
    }

    @Override
    public Player getByExternalId(UUID externalId) {
        return findByExternalId(externalId)
            .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public Page<Player> getAll(Pageable pageable) {
        return playerRepo.findAll(pageable);
    }

    @Override
    public List<Score> getScoresByPlayerExternalId(UUID playerExternalId) {
        Player player = getByExternalId(playerExternalId);
        List<Score> scores = scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId());
        return scores;
    }

    @Override
    public List<Game> getGamesByPlayerExternalId(UUID playerExternalId) {
        List<Game> games = gameRepo.findAllByPlayerIdOrderByUpdatedAtDesc(playerExternalId);
        return games;
    }

    @Override
    public List<Clash> getClashesByPlayerExternalId(UUID playerExternalId) {
        List<Clash> clashes = clashRepo.findAllByPlayerIdsContainsOrderByUpdatedAtDesc(playerExternalId);
        return clashes;
    }

    @Override
    public List<Log> getLogsByPlayerExternalId(UUID playerExternalId) {
        Player player = getByExternalId(playerExternalId);
        List<Log> logs = logRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId());
        return logs;
    }

    @Override
    public List<Ticket> getTicketsByPlayerExternalId(UUID playerExternalId) {
        Player player = getByExternalId(playerExternalId);
        List<Ticket> tickets = ticketRepo.findAllByPlayerIdOrderByUpdatedAtDesc(player.getId());
        return tickets;
    }

    @Override
    public Player loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("error.not_found.player"));
        return player;
    }

    @Override
    public PlayerStats getPlayerStatsByExternalId(UUID externalId) {
        PlayerStats stats = new PlayerStats();
        Long playerId = getByExternalId(externalId).getId();

        Optional<Score> latestScore = scoreRepo.findTop1ByPlayerIdOrderByCreatedAtDesc(playerId);
        if (latestScore.isPresent()) {
            stats.setLastActivity(latestScore.get().getCreatedAt());
        }
        Double averageScore = scoreRepo.findAverageValueByPlayerId(playerId);
        if (averageScore == null) {
            averageScore = 0.0;
        }
        stats.setAverageScore(averageScore);
        stats.setHighScore(scoreRepo.findTop1ByPlayerIdOrderByValueDesc(playerId).orElse(null));
        stats.setScoreCount(scoreRepo.countByPlayerId(playerId));

        return stats;
    }

    @Override
    public GlobalPlayerStats getGlobalStats() {
        
        GlobalPlayerStats globalStats = new GlobalPlayerStats();
        globalStats.setPlayerCount(playerRepo.count());

        Optional<Player> playerWithMostScores = playerRepo.findPlayerWithMostScores(PageRequest.of(0, 1)).stream().findFirst();
        if (playerWithMostScores.isPresent()) {
            globalStats.setMostScoresByAnyPlayer(scoreRepo.countByPlayerId(playerWithMostScores.get().getId()));
            globalStats.setPlayerWithMostScores(playerWithMostScores.get());
            globalStats.setMostScoresByAnyPlayer(scoreRepo.countByPlayerId(playerWithMostScores.get().getId()));
        }

        Optional<Player> playerWithHighestAverageScore = playerRepo.findPlayerWithHighestAverageScore(PageRequest.of(0, 1)).stream().findFirst();
        if (playerWithHighestAverageScore.isPresent()) {
            globalStats.setHighestAverageScoreByAnyPlayer(scoreRepo.findAverageValueByPlayerId(playerWithHighestAverageScore.get().getId()));
            globalStats.setPlayerWithHighestAverageScore(playerWithHighestAverageScore.get());
        }

        Optional<Score> highScore = scoreRepo.findTop1ByOrderByValueDesc();
        if (highScore.isPresent()) {
            globalStats.setHighScore(highScore.get());
        }
        globalStats.setNewestPlayer(playerRepo.findTop1ByOrderByCreatedAtDesc().orElse(null));
        globalStats.setOldestPlayer(playerRepo.findTop1ByOrderByCreatedAtAsc().orElse(null));

        return globalStats;
    }

    @Override
    public PlayerPreferences getPreferencesByPlayerExternalId(UUID playerExternalId) {
        checkPermission(playerExternalId);

        
        Player player = getByExternalId(playerExternalId);
        if (player.getPreferences() == null) {
            throw new ResourceNotFoundException("error.not_found.preferences");
        }

        return player.getPreferences();
    }

    @Override
    public PlayerPreferences setPreferencesByPlayerExternalId(UUID playerExternalId, PlayerPreferences playerPreferences) {
        checkPermission(playerExternalId);
        
        Player player = getByExternalId(playerExternalId);
        PlayerPreferences preferences = player.getPreferences();

        if (preferences != null) {
            preferences.setTheme(playerPreferences.getTheme());
            preferences.setLanguage(playerPreferences.getLanguage());
        } else {
            preferences = PlayerPreferences.getInstance(
                player,
                playerPreferences.getTheme(),
                playerPreferences.getLanguage()
            );
            player.setPreferences(preferences);
        }

        playerRepo.save(player);
        return player.getPreferences();
    }

    public Player updateUsernameByExternalId(UUID externalId, String username) {
        Player player = getByExternalId(externalId);
    
        validateUsername(username);
        player.setUsername(username);
    
        return playerRepo.save(player);
    }

    public Player updateEmailByExternalId(UUID externalId, String email) {
        Player player = getByExternalId(externalId);
        String normalizedEmail = EmailManager.normalizeEmail(email);
        validateEmail(normalizedEmail);
        player.setEmail(normalizedEmail);
        return playerRepo.save(player);
    }

    private void validateEmail(String email) {
        if (playerRepo.existsByEmailIgnoreCaseAndEmailVerified(email, true)) {
            throw new IllegalArgumentException("error.email_taken");
        }
    }

    private void validateUsername(String username) {
        if (playerRepo.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("error.username_taken");
        }
    }

    private void checkPermission(UUID playerExternalId) {
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer();  
        if (playerExternalId == null || authenticatedPlayer != null && !authenticatedPlayer.getExternalId().equals(playerExternalId)) {
            throw new AccessDeniedException("error.access_denied");
        }
    }

    @Override
    public void deleteInactivePlayers() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Player getCurrentPlayer() {
        return AuthContext.getAuthenticatedPlayer();
    }

    @Override
    public List<PlayerRelationship> getRelationshipsByPlayerExternalId(UUID playerExternalId) {
        Player player = getByExternalId(playerExternalId);
        return relationshipRepo.getRelationshipsByPlayerId(player.getId());
    }

    @Override
    public void mergePlayers(UUID parentExternalId, List<UUID> playerExternalIds) {
        
        Player parentPlayer = getByExternalId(parentExternalId);
        List<Player> players = playerRepo.findAllByExternalIdInOrderByUpdatedAtDesc(playerExternalIds);

        for (Player player : players) {
            List<Score> scoresToMerge = scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId());
            scoresToMerge.forEach(score -> score.setPlayer(parentPlayer));
            scoreRepo.saveAll(scoresToMerge);

            List<Clash> clashesToMerge = clashRepo.findAllByPlayerIdsContainsOrderByUpdatedAtDesc(player.getExternalId());
            clashesToMerge.forEach(clash -> clash.replacePlayer(player.getExternalId(), parentPlayer.getExternalId()));
            clashRepo.saveAll(clashesToMerge);

            List<Log> logsToMerge = logRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId());
            logsToMerge.forEach(log -> log.setPlayer(parentPlayer));
            logRepo.saveAll(logsToMerge);

            List<PlayerRelationship> relationshipsToMerge = relationshipRepo.getRelationshipsByPlayerId(player.getId());
            relationshipsToMerge.forEach(relationship -> {
                if (player.getId() == relationship.getId().getPlayer().getId()) {
                    relationship.getId().setPlayer(parentPlayer);
                } else if (player.getId() == relationship.getId().getRelatedPlayer().getId()) {
                    relationship.getId().setRelatedPlayer(parentPlayer);
                }
            });
            relationshipRepo.saveAll(relationshipsToMerge);
        }

        playerRepo.deleteAll(players);
        playerRepo.save(parentPlayer);
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        Player player = getByExternalId(externalId);
        playerRepo.delete(player);
    }

}