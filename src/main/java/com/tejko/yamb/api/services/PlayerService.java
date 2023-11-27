package com.tejko.yamb.api.services;

import java.util.List;

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

import com.tejko.yamb.models.Score;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.repositories.ScoreRepository;


import com.tejko.yamb.repositories.PlayerRepository;

@Service
public class PlayerService implements UserDetailsService {

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    ScoreRepository scoreRepo;

    public Player getById(Long id) {
        return playerRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
    }

    public List<Player> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return playerRepo.findAll(pageable).getContent();
    }

    public List<Score> getScoresByPlayerId(Long id) {
        Player player = playerRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return player.getScores();
    }

	@Override
    @Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Player player = playerRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User " + username + " is not found!"));
		return Player.build(player);
	}

}