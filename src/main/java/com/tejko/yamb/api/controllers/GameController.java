package com.tejko.yamb.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public List<GameResponse> getAll() {
		return gameService.getAll();
	}

	@GetMapping("/{id}")
	public GameResponse getById(@PathVariable String id) {
		return gameService.getById(id);
	}

	@PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public GameResponse create(@RequestBody GameRequest gameRequest) {
		return gameService.create(gameRequest);
	}

	@PutMapping("/{id}/roll")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public GameResponse rollById(@PathVariable String id, @RequestBody ActionRequest action) {
		return  gameService.rollById(id, action);
	}

	@PutMapping("/{id}/announce")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public GameResponse announceById(@PathVariable String id, @RequestBody ActionRequest action) {
		return  gameService.announceById(id, action);
	}

	@PutMapping("/{id}/fill")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public GameResponse fillById(@PathVariable String id, @RequestBody ActionRequest action) {
		return gameService.fillById(id, action);
	}

	@PutMapping("/{id}/restart")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public GameResponse restartById(@PathVariable String id) {
		return gameService.restartById(id);
	}

}
