package com.tejko.yamb.domain.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.GlobalScoreStats;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.util.I18nUtil;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.services.interfaces.ScoreService;

@Service
public class ScoreServiceImpl implements ScoreService {

	private final ScoreRepository scoreRepo;
	private final CustomObjectMapper mapper;
	private final I18nUtil i18nUtil;

	@Autowired
	public ScoreServiceImpl(ScoreRepository scoreRepo, CustomObjectMapper mapper, I18nUtil i18nUtil) {
		this.scoreRepo = scoreRepo;
		this.mapper = mapper;
		this.i18nUtil = i18nUtil;
	}

	@Override
	public Score fetchById(Long id) {
		return scoreRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(i18nUtil.getMessage("error.not_found.score")));
	}

    @Override
	public ScoreResponse getById(Long id) {
		Score score = fetchById(id);
		return mapper.mapToResponse(score);
	}

    @Override
	public List<ScoreResponse> getAll() {
		List<Score> scores = scoreRepo.findAll();
        return mapper.mapCollection(scores, mapper::mapToResponse, ArrayList::new);
	}

    @Override
	public void deleteById(Long id) {
		scoreRepo.deleteById(id);
	}

    @Override
	public GlobalScoreStats getGlobalStats() {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
		LocalDateTime startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
		LocalDateTime startOfYear = today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();

		GlobalScoreStats globalStats = new GlobalScoreStats();
		globalStats.setScoreCount(scoreRepo.count());
		globalStats.setAverageScore(scoreRepo.findAverageValue());
		globalStats.setHighScore(scoreRepo.findTop1ByOrderByValueDesc().map(mapper::mapToResponse).orElse(null));
		globalStats.setTopToday(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfToday, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		globalStats.setTopThisWeek(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfWeek, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		globalStats.setTopThisMonth(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfMonth, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		globalStats.setTopThisYear(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfYear, now).stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		globalStats.setTopAllTime(scoreRepo.findTop30ByOrderByValueDesc().stream().map(score -> mapper.mapToResponse(score)).collect(Collectors.toList()));
		
		
		return globalStats;
	}

}
