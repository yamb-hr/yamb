package com.tejko.yamb.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ScoreService scoreService;

	@GetMapping("/{id}")
	public ScoreResponse getById(@PathVariable Long id) {
		return scoreService.getById(id);
	}

	@GetMapping("")
	public List<ScoreResponse> getAll() {
		return scoreService.getAll();
	}

	@GetMapping("/stats")
	public GlobalScoreStats getGlobalStats() {
		return scoreService.getGlobalStats();
	}

}
