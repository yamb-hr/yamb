package com.tejko.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.constants.GameConstants;
import com.tejko.models.Score;
import com.tejko.repositories.ScoreRepository;

@Service
public class ScoreService {

	@Autowired
	ScoreRepository scoreRepo;

	public Score getById(Long id) {
		return scoreRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_SCORE_NOT_FOUND));
	}

	public List<Score> getAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
		return scoreRepo.findAll(pageable).getContent();
	}

}
