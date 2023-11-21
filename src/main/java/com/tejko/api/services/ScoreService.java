package com.tejko.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.tejko.models.Score;
import com.tejko.repositories.ScoreRepository;

@Service
public class ScoreService {

	@Autowired
	ScoreRepository scoreRepository;

	public Score getById(UUID id) {
		return scoreRepository.getById(id);
	}

	public List<Score> getAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
		return scoreRepository.findAll(pageable).getContent();
	}
	
    public List<Score> getScoresByPlayerId(UUID playerId) {
        return scoreRepository.findAllByPlayerId(playerId);
    }

}
