package com.tejko.yamb.api.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.assemblers.ScoreModelAssembler;
import com.tejko.yamb.api.dto.responses.GlobalScoreStatsResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.business.interfaces.ScoreService;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.util.SortFieldTranslator;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

	private final ScoreService scoreService;
	private final ScoreModelAssembler scoreModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public ScoreController(ScoreService scoreService, ScoreModelAssembler scoreModelAssembler, SortFieldTranslator sortFieldTranslator) {
		this.scoreService = scoreService;
		this.scoreModelAssembler = scoreModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}

	@GetMapping("/{externalId}")
	public ResponseEntity<ScoreResponse> getByExternalId(@PathVariable UUID externalId) {
		ScoreResponse scoreResponse = scoreModelAssembler.toModel(scoreService.getByExternalId(externalId));
		return ResponseEntity.ok(scoreResponse);
	}

	@GetMapping("")
	public ResponseEntity<PagedModel<ScoreResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Score.class, ScoreResponse.class);
		PagedModel<ScoreResponse> pagedScores = scoreModelAssembler.toPagedModel(scoreService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedScores);
	}

	@GetMapping("/stats")
	public ResponseEntity<GlobalScoreStatsResponse> getGlobalStats() {
		GlobalScoreStatsResponse globalScoreStatsResponse = scoreModelAssembler.toModel(scoreService.getGlobalStats());
		return ResponseEntity.ok(globalScoreStatsResponse);
	}

}
