package com.tejko.yamb.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.api.dto.responses.GlobalScoreStats;
import com.tejko.yamb.domain.services.interfaces.ScoreService;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

	private final ScoreService scoreService;

	@Autowired
	public ScoreController(ScoreService scoreService) {
		this.scoreService = scoreService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScoreResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(scoreService.getById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<ScoreResponse>> getAll() {
		return ResponseEntity.ok(scoreService.getAll());
	}

	@GetMapping("/stats")
	public ResponseEntity<GlobalScoreStats> getGlobalStats() {
		return ResponseEntity.ok(scoreService.getGlobalStats());
	}

}
