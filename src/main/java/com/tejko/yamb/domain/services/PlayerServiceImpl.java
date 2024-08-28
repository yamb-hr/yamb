package com.tejko.yamb.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStats;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStats;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.util.I18nUtil;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.services.interfaces.PlayerService;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;
    private final CustomObjectMapper mapper;
    private final I18nUtil i18nUtil;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepo, ScoreRepository scoreRepo, CustomObjectMapper mapper, I18nUtil i18nUtil) {
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
        this.mapper = mapper;
        this.i18nUtil = i18nUtil;
    }

    @Override
    public Player fetchById(java.lang.Long id) {
        return playerRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(i18nUtil.getMessage("error.not_found.player")));
    }

    @Override
    public PlayerResponse getById(Long id) {
        Player player = fetchById(id);
        return mapper.mapToResponse(player);
    }

    @Override
    public List<PlayerResponse> getAll() {
        List<Player> players = playerRepo.findAll();
        return mapper.mapCollection(players, mapper::mapToResponse, ArrayList::new);
    }

    @Override
    public List<ScoreResponse> getScoresByPlayerId(Long playerId) {
        List<Score> scores = scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(playerId);
        return mapper.mapCollection(scores, mapper::mapToResponse, ArrayList::new);
    }

    @Override
    public Player loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(i18nUtil.getMessage("error.not_found.player")));
        return player;
    }

    @Override
    public PlayerStats getPlayerStats(Long id) {
        PlayerStats stats = new PlayerStats();

        stats.setLastActivity(scoreRepo.findTop1ByPlayerIdOrderByCreatedAtDesc(id).get().getCreatedAt());
        stats.setAverageScore(scoreRepo.findAverageValueByPlayerId(id));
        stats.setHighScore(scoreRepo.findTop1ByPlayerIdOrderByValueDesc(id).map(mapper::mapToResponse).orElse(null));
        stats.setScoreCount(scoreRepo.countByPlayerId(id));

        return stats;
    }

    @Override
    public GlobalPlayerStats getGlobalStats() {
        
        GlobalPlayerStats globalStats = new GlobalPlayerStats();
        globalStats.setPlayerCount(playerRepo.count());

        Optional<Player> playerWithMostScores = playerRepo.findPlayerWithMostScores(PageRequest.of(0, 1)).stream().findFirst();
        globalStats.setPlayerWithMostScores(playerWithMostScores.map(mapper::mapToResponse).orElse(null));
        globalStats.setMostScoresByAnyPlayer(playerWithMostScores.map(player -> scoreRepo.countByPlayerId(player.getId())).orElse(null));

        Optional<Player> playerWithHighestAverageScore = playerRepo.findPlayerWithHighestAverageScore(PageRequest.of(0, 1)).stream().findFirst();
        globalStats.setHighestAverageScoreByAnyPlayer(playerWithHighestAverageScore.map(player -> scoreRepo.findAverageValueByPlayerId(player.getId())).orElse(null));
        globalStats.setPlayerWithHighestAverageScore(playerWithHighestAverageScore.map(mapper::mapToResponse).orElse(null));

        Optional<Score> highScore = scoreRepo.findTop1ByOrderByValueDesc();
        globalStats.setHighScore(highScore.map(mapper::mapToResponse).orElse(null));
        globalStats.setNewestPlayer(playerRepo.findTop1ByOrderByCreatedAtDesc().map(mapper::mapToResponse).orElse(null));
        globalStats.setOldestPlayer(playerRepo.findTop1ByOrderByCreatedAtAsc().map(mapper::mapToResponse).orElse(null));

        return globalStats;
    }

    @Override
    public PlayerPreferencesResponse getPreferencesByPlayerId(Long playerId) {
        checkPermission(playerId);
        
        Player player = fetchById(playerId);
        if (player.getPreferences() == null) {
            throw new ResourceNotFoundException(i18nUtil.getMessage("error.not_found.preferences"));
        }

        return mapper.mapToResponse(player.getPreferences());
    }

    @Override
    public PlayerPreferencesResponse setPreferencesByPlayerId(Long playerId, PlayerPreferencesRequest playerPreferencesRequest) {
        checkPermission(playerId);

        Player player = fetchById(playerId);
        PlayerPreferences preferences = player.getPreferences();

        if (preferences != null) {
            preferences.setTheme(playerPreferencesRequest.getTheme());
            preferences.setLanguage(playerPreferencesRequest.getLanguage());
        } else {
            preferences = PlayerPreferences.getInstance(
                player,
                playerPreferencesRequest.getTheme(),
                playerPreferencesRequest.getLanguage()
            );
            player.setPreferences(preferences);
        }

        playerRepo.save(player);
        return mapper.mapToResponse(player.getPreferences());
    }

    private void checkPermission(Long playerId) {
        Optional<Player> authenticatedPlayerId = AuthContext.getAuthenticatedPlayer();  
        if (playerId == null || !authenticatedPlayerId.isPresent() || !authenticatedPlayerId.get().getId().equals(playerId)) {
            throw new AccessDeniedException(i18nUtil.getMessage("error.access_denied"));
        }
    }

    @Override
    public void deleteInactivePlayers() {
        throw new NotImplementedException("deleteInactivePlayers");
    }

}