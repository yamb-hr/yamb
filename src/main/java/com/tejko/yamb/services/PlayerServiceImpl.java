package com.tejko.yamb.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.api.payload.responses.PlayerStatsResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.services.PlayerService;
import com.tejko.yamb.interfaces.services.WebSocketService;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private ScoreRepository scoreRepo;

    @Autowired
    private WebSocketService webSocketService;

    public Player getByExternalId(UUID externalId) {
        return playerRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
    }

    public List<Player> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return playerRepo.findAll(pageable).getContent();
    }

    public List<Score> getScoresByPlayerId(UUID externalId) {
        Player player = playerRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return scoreRepo.findAllByPlayerIdOrderByCreatedAtDesc(player.getId());
    }

    public Player loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return player;
    }

    public void deleteByExternalId(UUID externalId) {
        playerRepo.deleteByExternalId(externalId);
    }

    public String getPrincipalByExternalId(UUID externalId) {
        return webSocketService.getPrincipalByExternalId(externalId);
    }

    public PlayerStatsResponse getPlayerStats(UUID externalId) {
        Player player = playerRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));

        PlayerStatsResponse stats = new PlayerStatsResponse();

        stats.lastActivity = playerRepo.findLastActivityByPlayerId(player.getId());
        stats.averageScore = scoreRepo.findAverageValueByPlayerId(player.getId());
        stats.topScore = scoreRepo.findTopValueByPlayerId(player.getId());
        stats.gamesPlayed = scoreRepo.countByPlayerId(player.getId());

        return stats;
    }

}