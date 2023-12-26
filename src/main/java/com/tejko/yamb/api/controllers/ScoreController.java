package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.ScoreService;
import com.tejko.yamb.models.payload.DashboardData;
import com.tejko.yamb.models.payload.DateTimeInterval;
import com.tejko.yamb.models.payload.dto.ScoreDTO;
import com.tejko.yamb.util.Mapper;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

	@Autowired
	ScoreService scoreService;

	@Autowired
	Mapper mapper;

	@GetMapping("/{id}")
	public ResponseEntity<ScoreDTO> getById(@PathVariable Long id) {
		return new ResponseEntity<>(mapper.toDTO(scoreService.getById(id)), HttpStatus.OK);
	}

	@GetMapping("")
	public List<ScoreDTO> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "desc") String direction) {
		return scoreService.getAll(page, size, sort, direction)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
	}

	@GetMapping("/interval")
	public List<ScoreDTO> getByInterval(@RequestBody DateTimeInterval interval) {
		return scoreService.getByInterval(interval)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
	}

	@GetMapping("/dashboard")
	public DashboardData getDashboardData() {
		return scoreService.getDashboardData();
	}

}
