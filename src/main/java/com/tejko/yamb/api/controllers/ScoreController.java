package com.tejko.yamb.api.controllers;

import java.util.List;

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
import com.tejko.yamb.models.Score;
import com.tejko.yamb.models.payload.DashboardResponse;
import com.tejko.yamb.models.payload.DateTimeInterval;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

	@Autowired
	ScoreService scoreService;

	@GetMapping("/{id}")
	public ResponseEntity<Score> getById(@PathVariable Long id) {
		return new ResponseEntity<>(scoreService.getById(id), HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<List<Score>> getAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort,
			@RequestParam(defaultValue = "desc") String direction) {
		return new ResponseEntity<>(scoreService.getAll(page, size, sort, direction), HttpStatus.OK);
	}

	@GetMapping("/interval")
	public ResponseEntity<List<Score>> getByInterval(@RequestBody DateTimeInterval interval) {
		return new ResponseEntity<>(scoreService.getByInterval(interval), HttpStatus.OK);
	}

	@GetMapping("/dashboard")
	public ResponseEntity<DashboardResponse> getDashboardData() {
		return new ResponseEntity<>(scoreService.getDashboardData(), HttpStatus.OK);
	}

}
