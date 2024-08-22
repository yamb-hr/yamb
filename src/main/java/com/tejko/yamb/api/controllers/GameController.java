package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.payload.requests.ActionRequest;
import com.tejko.yamb.api.payload.responses.GameResponse;
import com.tejko.yamb.interfaces.BaseController;
import com.tejko.yamb.interfaces.services.GameService;
import com.tejko.yamb.util.PayloadMapper;

@RestController
@RequestMapping("/api/games")
public class GameController implements BaseController<GameResponse> {

	@Autowired
	private GameService gameService;

	@Autowired
	private PayloadMapper mapper;

	@GetMapping("")
	public List<GameResponse> getAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sort,
			@RequestParam(defaultValue = "desc") String direction) {
		return gameService.getAll(page, size, sort, direction)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/{externalId}")
	public GameResponse getByExternalId(@PathVariable UUID externalId) {
		return mapper.toDTO(gameService.getByExternalId(externalId));
	}

	@PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public GameResponse play() {
		return mapper.toDTO(gameService.play());
	}

	@PutMapping("/{externalId}/roll")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #externalId)")
	public GameResponse rollByExternalId(@PathVariable UUID externalId, @RequestBody ActionRequest action) {
		return  mapper.toDTO(gameService.rollByExternalId(externalId, action));
	}

	@PutMapping("/{externalId}/announce")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #externalId)")
	public GameResponse announceByExternalId(@PathVariable UUID externalId, @RequestBody ActionRequest action) {
		return  mapper.toDTO(gameService.announceByExternalId(externalId, action));
	}

	@PutMapping("/{externalId}/fill")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #externalId)")
	public GameResponse fillByExternalId(@PathVariable UUID externalId, @RequestBody ActionRequest action) {
		return mapper.toDTO(gameService.fillByExternalId(externalId, action));
	}

	@PutMapping("/{externalId}/restart")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #externalId)")
	public GameResponse restartByExternalId(@PathVariable UUID externalId) {
		return mapper.toDTO(gameService.restartByExternalId(externalId));
	}

	@DeleteMapping("/{externalId}")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #externalId)")
	public void deleteByExternalId(@PathVariable UUID externalId) {
		gameService.deleteByExternalId(externalId);
	}

}
