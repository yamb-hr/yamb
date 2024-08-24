package com.tejko.yamb.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.api.payload.responses.PlayerResponse;
import com.tejko.yamb.api.payload.responses.PlayerStatsResponse;
import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.services.PlayerService;
import com.tejko.yamb.util.ObjectMapper;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepo;
    private final ScoreRepository scoreRepo;
    private final ObjectMapper mapper;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepo, ScoreRepository scoreRepo, ObjectMapper mapper) {
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
    public PlayerStatsResponse getPlayerStats(Long id) {
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));

        PlayerStatsResponse stats = new PlayerStatsResponse();

        stats.setLastActivity(playerRepo.findLastActivityByPlayerId(player.getId()));
        stats.setAverageScore(scoreRepo.findAverageValueByPlayerId(player.getId()));
        stats.setTopScore(scoreRepo.findTopValueByPlayerId(player.getId()));
        stats.setGamesPlayed(scoreRepo.countByPlayerId(player.getId()));

        return stats;
    }

}