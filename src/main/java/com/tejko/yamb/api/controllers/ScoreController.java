package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.payload.requests.DateRangeRequest;
import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.api.payload.responses.ScoreboardResponse;
import com.tejko.yamb.interfaces.BaseController;
import com.tejko.yamb.interfaces.services.ScoreService;
import com.tejko.yamb.util.PayloadMapper;

@RestController
@RequestMapping("/api/scores")
public class ScoreController implements BaseController<ScoreResponse> {

	@Autowired
	private ScoreService scoreService;

	@Autowired
	private PayloadMapper mapper;

	@GetMapping("/{externalId}")
	public ScoreResponse getByExternalId(@PathVariable UUID externalId) {
		return mapper.toDTO(scoreService.getByExternalId(externalId));
	}

	@GetMapping("")
	public List<ScoreResponse> getAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sort,
			@RequestParam(defaultValue = "desc") String direction) {
		return scoreService.getAll(page, size, sort, direction)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/interval")
	public List<ScoreResponse> getByInterval(@RequestBody DateRangeRequest interval) {
		return scoreService.getByInterval(interval)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/scoreboard")
	public ScoreboardResponse getScoreboard() {
		return scoreService.getScoreboard();
	}

	@DeleteMapping("/{externalId}")
	public void deleteByExternalId(@PathVariable UUID externalId) {
		scoreService.deleteByExternalId(externalId);
	}

}
