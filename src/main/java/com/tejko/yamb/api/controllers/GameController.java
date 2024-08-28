package com.tejko.yamb.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.domain.services.interfaces.GameService;

@RestController
@RequestMapping("/api/games")
public class GameController {

	private final GameService gameService;

	@Autowired
	public GameController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("")
	public ResponseEntity<List<GameResponse>> getAll() {
		return ResponseEntity.ok(gameService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<GameResponse> getById(@PathVariable String id) {
		return ResponseEntity.ok(gameService.getById(id));
	}

	@PutMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> getOrCreate(@Valid @RequestBody GameRequest gameRequest) {
		GameResponse response = gameService.getOrCreate(gameRequest);
		HttpStatus status = response.isNew() ? HttpStatus.CREATED : HttpStatus.OK;
		return new ResponseEntity<>(response, status);
	}

	@PutMapping("/{id}/roll")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> rollById(@PathVariable String id, @Valid @RequestBody ActionRequest action) {
		return ResponseEntity.ok(gameService.rollById(id, action));
	}

	@PutMapping("/{id}/announce")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> announceById(@PathVariable String id, @Valid @RequestBody ActionRequest action) {
		return ResponseEntity.ok(gameService.announceById(id, action));
	}

	@PutMapping("/{id}/fill")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> fillById(@PathVariable String id, @Valid @RequestBody ActionRequest action) {
		return ResponseEntity.ok(gameService.fillById(id, action));
	}

	@PutMapping("/{id}/restart")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> restartById(@PathVariable String id) {
		return ResponseEntity.ok(gameService.restartById(id));
	}

	@PutMapping("/{id}/finish")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> finishById(@PathVariable String id) {
		return ResponseEntity.ok(gameService.finishById(id));
	}
	
	@PutMapping("/{id}/complete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<GameResponse> completeById(@PathVariable String id) {
		return ResponseEntity.ok(gameService.completeById(id));
	}

}
