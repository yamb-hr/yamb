package com.tejko.yamb.domain.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.ScoreboardResponse;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.services.interfaces.ScoreService;

@Service
public class ScoreServiceImpl implements ScoreService {

	private final ScoreRepository scoreRepo;
	private final CustomObjectMapper mapper;

	@Autowired
	public ScoreServiceImpl(ScoreRepository scoreRepo, CustomObjectMapper mapper) {
		this.scoreRepo = scoreRepo;
		this.mapper = mapper;
	}

	@Override
	public Score fetchById(Long id) {
		return scoreRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_SCORE_NOT_FOUND));
	}

    @Override
	public ScoreResponse getById(Long id) {
		Score score = fetchById(id);
		return mapper.mapToResponse(score);
	}

    @Override
	public List<ScoreResponse> getAll() {
		List<Score> scores = scoreRepo.findAll();
		return scores.stream().map(mapper::mapToResponse).collect(Collectors.toList());
	}

    @Override
	public void deleteById(Long id) {
		scoreRepo.deleteById(id);
	}

    @Override
	public ScoreboardResponse getScoreboard() {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
		LocalDateTime startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
		LocalDateTime startOfYear = today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();

		ScoreboardResponse scoreboard = new ScoreboardResponse();
		scoreboard.setTopToday(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfToday, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		scoreboard.setTopThisWeek(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfWeek, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		scoreboard.setTopThisMonth(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfMonth, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		scoreboard.setTopThisYear(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfYear, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		scoreboard.setTopAllTime(scoreRepo.findTop30ByOrderByValueDesc().stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		scoreboard.setGamesPlayed(scoreRepo.count());
		scoreboard.setAverageScore(scoreRepo.findAverageValue());
		
		return scoreboard;
	}

}
