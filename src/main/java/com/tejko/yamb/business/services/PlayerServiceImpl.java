package com.tejko.yamb.business.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.PlayerStats;
import com.tejko.yamb.domain.models.entities.Player;
import com.tejko.yamb.domain.models.entities.PlayerPreferences;
import com.tejko.yamb.domain.models.entities.Score;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.security.AuthContext;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepo, ScoreRepository scoreRepo) {
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
    }

    @Override
    public Player getById(java.lang.Long id) {
        return playerRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public List<Player> getAll() {
        List<Player> players = playerRepo.findAllByOrderByCreatedAtDesc();
        return players;
    }

    @Override
    public List<Score> getScoresByPlayerId(Long playerId) {
        List<Score> scores = scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(playerId);
        return scores;
    }

    @Override
    public Player loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("error.not_found.player"));
        return player;
    }

    @Override
    public PlayerStats getPlayerStats(Long id) {
        PlayerStats stats = new PlayerStats();

        Optional<Score> latestScore = scoreRepo.findTop1ByPlayerIdOrderByCreatedAtDesc(id);
        if (latestScore.isPresent()) {
            stats.setLastActivity(latestScore.get().getCreatedAt());
        }
        Double averageScore = scoreRepo.findAverageValueByPlayerId(id);
        if (averageScore == null) {
            averageScore = 0.0;
        }
        stats.setAverageScore(averageScore);
        stats.setHighScore(scoreRepo.findTop1ByPlayerIdOrderByValueDesc(id).orElse(null));
        stats.setScoreCount(scoreRepo.countByPlayerId(id));

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
    public PlayerPreferences getPreferencesByPlayerId(Long playerId) {
        checkPermission(playerId);
        
        Player player = getById(playerId);
        if (player.getPreferences() == null) {
            throw new ResourceNotFoundException("error.not_found.preferences");
        }

        return player.getPreferences();
    }

    @Override
    public PlayerPreferences setPreferencesByPlayerId(Long playerId, PlayerPreferences playerPreferences) {
        checkPermission(playerId);

        Player player = getById(playerId);
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

    public Player changeUsername(Long playerId, String username) {
        Player player = playerRepo.getById(playerId);
    
        validateUsername(username);
        player.setUsername(username);
    
        return playerRepo.save(player);
    }

    private void validateUsername(String username) {
        if (playerRepo.existsByUsername(username)) {
            throw new IllegalArgumentException("error.username_taken");
        }
    }

    private void checkPermission(Long playerId) {
        Optional<Player> authenticatedPlayerId = AuthContext.getAuthenticatedPlayer();  
        if (playerId == null || !authenticatedPlayerId.isPresent() || !authenticatedPlayerId.get().getId().equals(playerId)) {
            throw new AccessDeniedException("error.access_denied");
        }
    }

    @Override
    public void deleteInactivePlayers() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

}