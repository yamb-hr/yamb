package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.responses.GlobalScoreStatsResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.business.interfaces.ScoreService;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

	private final ScoreService scoreService;
	private final ModelMapper modelMapper;

	@Autowired
	public ScoreController(ScoreService scoreService, ModelMapper modelMapper) {
		this.scoreService = scoreService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScoreResponse> getById(@PathVariable Long id) {
		ScoreResponse scoreResponse = modelMapper.map(scoreService.getById(id), ScoreResponse.class);
		return ResponseEntity.ok(scoreResponse);
	}

	@GetMapping("")
	public ResponseEntity<List<ScoreResponse>> getAll() {
		List<ScoreResponse> scoreResponses = scoreService.getAll().stream().map(score -> modelMapper.map(score, ScoreResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(scoreResponses);
	}

	@GetMapping("/stats")
	public ResponseEntity<GlobalScoreStatsResponse> getGlobalStats() {
		GlobalScoreStatsResponse globalScoreStatsResponse = modelMapper.map(scoreService.getGlobalStats(), GlobalScoreStatsResponse.class);
		return ResponseEntity.ok(globalScoreStatsResponse);
	}

}
