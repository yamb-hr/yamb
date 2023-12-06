package com.tejko.yamb.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.models.Score;
import com.tejko.yamb.models.payload.DateTimeInterval;
import com.tejko.yamb.repositories.ScoreRepository;

@Service
public class ScoreService {

	@Autowired
	ScoreRepository scoreRepo;

	public Score getById(Long id) {
		return scoreRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_SCORE_NOT_FOUND));
	}

	public List<Score> getAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
		return scoreRepo.findAll(pageable).getContent();
	}

	public List<Score> getByInterval(DateTimeInterval interval) {
		return scoreRepo.findByDateBetween(interval.getFrom(), interval.getTo());
	}

}
