package com.tejko.yamb.api.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.interfaces.RestService;
import com.tejko.yamb.models.Score;
import com.tejko.yamb.models.payload.DashboardData;
import com.tejko.yamb.models.payload.DateTimeInterval;
import com.tejko.yamb.repositories.ScoreRepository;

@Service
public class ScoreService implements RestService<Score> {

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

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
	}

	public DashboardData getDashboardData() {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
		LocalDateTime startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
		LocalDateTime startOfYear = today.with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
		List<Score> topToday = scoreRepo.findTop15ByDateBetweenOrderByValueDesc(startOfToday, now);
		List<Score> topThisWeek = scoreRepo.findTop15ByDateBetweenOrderByValueDesc(startOfWeek, now);
		List<Score> topThisMonth = scoreRepo.findTop15ByDateBetweenOrderByValueDesc(startOfMonth, now);
		List<Score> topThisYear = scoreRepo.findTop15ByDateBetweenOrderByValueDesc(startOfYear, now);
		List<Score> topAllTime = scoreRepo.findTop15ByOrderByValueDesc();
		return new DashboardData(topToday, topThisWeek, topThisMonth, topThisYear, topAllTime);
	}

}
