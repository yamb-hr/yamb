package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.services.PlayerService;
import com.tejko.yamb.api.payload.responses.PlayerResponse;
import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.interfaces.BaseController;
import com.tejko.yamb.util.Mapper;

@RestController
@RequestMapping("/api/players")
public class PlayerController implements BaseController<PlayerResponse> {

	@Autowired
	PlayerService playerService;

	@Autowired
	Mapper mapper;

	@GetMapping("/{externalId}")
	public PlayerResponse getByExternalId(@PathVariable UUID externalId) {
		return mapper.toDTO(playerService.getByExternalId(externalId));
	}

	@GetMapping("")
	public List<PlayerResponse> getAll(@RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "createdAt") String sort,
			@RequestParam(defaultValue = "desc") String direction) {
		return playerService.getAll(page, size, sort, direction)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@GetMapping("/{externalId}/scores")
	public List<ScoreResponse> getScoresByPlayerId(@PathVariable UUID externalId) {
		return playerService.getScoresByPlayerId(externalId)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@DeleteMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteByExternalId(@PathVariable UUID externalId) {
		playerService.deleteByExternalId(externalId);
	}

	@GetMapping("/{externalId}/principal")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasPlayerPermission(authentication, #externalId)")
	public String getPrincipalById(@PathVariable UUID externalId) {
		return playerService.getPrincipalByExternalId(externalId);
	}

}
