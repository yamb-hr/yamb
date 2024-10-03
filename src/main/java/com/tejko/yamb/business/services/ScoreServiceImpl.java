package com.tejko.yamb.business.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.ScoreService;
import com.tejko.yamb.domain.models.GlobalScoreStats;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.ScoreRepository;

@Service
public class ScoreServiceImpl implements ScoreService {

	private final ScoreRepository scoreRepo;

	@Autowired
	public ScoreServiceImpl(ScoreRepository scoreRepo) {
		this.scoreRepo = scoreRepo;
	}

	@Override
	public Score getByExternalId(UUID externalId) {
		return scoreRepo.findByExternalId(externalId)
			.orElseThrow(() -> new ResourceNotFoundException("error.not_found.score"));
	}

    @Override
	public Page<Score> getAll(Pageable pageable) {
		return scoreRepo.findAll(pageable);
	}

    @Override
	public void deleteByExternalId(UUID externalId) {
		scoreRepo.deleteByExternalId(externalId);
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
		globalStats.setHighScore(scoreRepo.findTop1ByOrderByValueDesc().orElse(null));
		globalStats.setTopToday(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfToday, now));
		globalStats.setTopThisWeek(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfWeek, now));
		globalStats.setTopThisMonth(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfMonth, now));
		globalStats.setTopThisYear(scoreRepo.findTop30ByCreatedAtBetweenOrderByValueDesc(startOfYear, now));
		globalStats.setTopAllTime(scoreRepo.findTop30ByOrderByValueDesc());
		
		
		return globalStats;
	}

}
