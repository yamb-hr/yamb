package com.tejko.yamb.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.responses.GlobalPlayerStats;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStats;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.services.interfaces.PlayerService;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;
    private final CustomObjectMapper mapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepo, ScoreRepository scoreRepo, CustomObjectMapper mapper) {
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
        this.mapper = mapper;
    }

    @Override
    public Player fetchById(java.lang.Long id) {
        return playerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
    }

    @Override
    public PlayerResponse getById(Long id) {
        Player player = fetchById(id);
        return mapper.mapToResponse(player);
    }

    @Override
    public List<PlayerResponse> getAll() {
        List<Player> players = playerRepo.findAll();
        return players.stream().map(mapper::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ScoreResponse> getScoresByPlayerId(Long id) {
        Player player = fetchById(id);
        List<Score> scores = scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId());
        return scores.stream().map(mapper::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public Player loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
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

}