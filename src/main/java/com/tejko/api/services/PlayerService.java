package com.tejko.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.models.Score;
import com.tejko.models.Player;
import com.tejko.repositories.ScoreRepository;
import com.tejko.repositories.PlayerRepository;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    ScoreRepository scoreRepository;

    public Player getById(UUID id) {
        return playerRepository.getById(id);
    }

    public List<Player> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return playerRepository.findAll(pageable).getContent();
    }

    public List<Score> getScoresByPlayerId(UUID id) {
        return scoreRepository.findAllByPlayerId(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username);
        
        if (player == null) {
            throw new UsernameNotFoundException("Player with username: " + username + " not found");
        }
        
        // Create and return the Spring Security UserDetails object
        return User.builder()
            .username(player.getUsername())
            .password(player.getPassword())
            .authorities("USER")
            .build();
    }

}