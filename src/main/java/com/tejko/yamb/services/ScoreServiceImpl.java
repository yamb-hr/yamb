package com.tejko.yamb.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.payload.requests.DateRangeRequest;
import com.tejko.yamb.api.payload.responses.ScoreboardResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.services.ScoreService;
import com.tejko.yamb.util.PayloadMapper;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.ScoreRepository;

@Service
public class ScoreServiceImpl implements ScoreService {

	@Autowired
	private ScoreRepository scoreRepo;

	@Autowired
	private PayloadMapper mapper;

	public Score getByExternalId(UUID externalId) {
		return scoreRepo.findByExternalId(externalId)
				.orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_SCORE_NOT_FOUND));
	}

	public List<Score> getAll(Integer page, Integer size, String sort, String direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
		return scoreRepo.findAll(pageable).getContent();
	}

	public List<Score> getByInterval(DateRangeRequest interval) {
		return scoreRepo.findByCreatedAtBetween(interval.getFrom(), interval.getTo());
	}

	public void deleteByExternalId(UUID externalId) {
		scoreRepo.deleteByExternalId(externalId);
	}

	public ScoreboardResponse getScoreboard() {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
		LocalDateTime startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
		LocalDateTime startOfYear = today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
		ScoreboardResponse scoreboard = new ScoreboardResponse();
		// convert to DTO
		scoreboard.topToday = scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfToday, now).stream().map(score -> mapper.toDTO(score)).collect(Collectors.toList());
		scoreboard.topThisWeek = scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfWeek, now).stream().map(score -> mapper.toDTO(score)).collect(Collectors.toList());
		scoreboard.topThisMonth = scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfMonth, now).stream().map(score -> mapper.toDTO(score)).collect(Collectors.toList());
		scoreboard.topThisYear = scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfYear, now).stream().map(score -> mapper.toDTO(score)).collect(Collectors.toList());
		scoreboard.topAllTime = scoreRepo.findTop30ByOrderByValueDesc().stream().map(score -> mapper.toDTO(score)).collect(Collectors.toList());
		scoreboard.gamesPlayed = scoreRepo.count();
		scoreboard.averageScore = scoreRepo.findAverageValue();
		return scoreboard;
	}

}
