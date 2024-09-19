package com.tejko.yamb.api.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.GameModelAssembler;
import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.business.interfaces.GameService;

@RestController
@RequestMapping("/api/games")
public class GameController {

	private final GameService gameService;
	private final GameModelAssembler gameModelAssembler;

	@Autowired
	public GameController(GameService gameService, GameModelAssembler gameModelAssembler) {
		this.gameService = gameService;
		this.gameModelAssembler = gameModelAssembler;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<GameResponse> getById(@PathVariable String id) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.getById(id));
		return ResponseEntity.ok(gameResponse);
	}

	@GetMapping("")
	public ResponseEntity<PagedModel<GameResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PagedModel<GameResponse> pagedGames = gameModelAssembler.toPagedModel(gameService.getAll(pageable));
		return ResponseEntity.ok(pagedGames);
	}

	@PutMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> getOrCreate() {

		GameResponse gameResponse = gameModelAssembler.toModel(gameService.getOrCreate());

		if (gameResponse.getCreatedAt().equals(gameResponse.getUpdatedAt())) {
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(gameResponse.getId())
				.toUri();
			return ResponseEntity.created(location).body(gameResponse);
		}

		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{id}/roll")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> rollById(@PathVariable String id, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.rollById(id, actionRequest.getDiceToRoll()));
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{id}/announce")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> announceById(@PathVariable String id, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.announceById(id, actionRequest.getBoxType()));
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{id}/fill")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> fillById(@PathVariable String id, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.fillById(id, actionRequest.getColumnType(), actionRequest.getBoxType()));
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{id}/restart")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> restartById(@PathVariable String id) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.restartById(id));
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{id}/archive")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> archiveById(@PathVariable String id) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.archiveById(id));
		return ResponseEntity.ok(gameResponse);
	}
	
	@PutMapping("/{id}/complete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<GameResponse> completeById(@PathVariable String id) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.completeById(id));
		return ResponseEntity.ok(gameResponse);
	}

}
