package com.tejko.yamb.services;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.BaseService;
import com.tejko.yamb.domain.repositories.ScoreRepository;

import com.tejko.yamb.domain.repositories.PlayerRepository;

@Service
public class PlayerService implements UserDetailsService, BaseService<Player> {

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    ScoreRepository scoreRepo;

    @Autowired
    WebSocketService webSocketService;

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
        return player.getScores();
    }

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " is not found!"));
        return Player.build(player);
    }

    public void deleteByExternalId(UUID externalId) {
        playerRepo.deleteByExternalId(externalId);
    }

    public String getPrincipalByExternalId(UUID externalId) {
        return webSocketService.getPrincipalByPlayerId(externalId);
    }

}