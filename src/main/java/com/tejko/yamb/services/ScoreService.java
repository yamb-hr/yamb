package com.tejko.yamb.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

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
import com.tejko.yamb.interfaces.BaseService;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.domain.repositories.ScoreRepository;

@Service
public class ScoreService implements BaseService<Score> {

	@Autowired
	ScoreRepository scoreRepo;

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

	@Override
	public void deleteByExternalId(UUID externalId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteByExternalId'");
	}

	public ScoreboardResponse getDashboardData() {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
		LocalDateTime startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
		LocalDateTime startOfYear = today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
		List<Score> topToday = scoreRepo.findTop15ByCreatedAtBetweenOrderByValueDesc(startOfToday, now);
		List<Score> topThisWeek = scoreRepo.findTop15ByCreatedAtBetweenOrderByValueDesc(startOfWeek, now);
		List<Score> topThisMonth = scoreRepo.findTop15ByCreatedAtBetweenOrderByValueDesc(startOfMonth, now);
		List<Score> topThisYear = scoreRepo.findTop15ByCreatedAtBetweenOrderByValueDesc(startOfYear, now);
		List<Score> topAllTime = scoreRepo.findTop15ByOrderByValueDesc();
		return new ScoreboardResponse(topToday, topThisWeek, topThisMonth, topThisYear, topAllTime);
	}

}
