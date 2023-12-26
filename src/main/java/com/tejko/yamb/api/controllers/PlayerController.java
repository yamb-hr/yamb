package com.tejko.yamb.api.controllers;

import java.util.List;
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

import com.tejko.yamb.api.services.PlayerService;
import com.tejko.yamb.models.payload.dto.PlayerDTO;
import com.tejko.yamb.models.payload.dto.ScoreDTO;
import com.tejko.yamb.util.Mapper;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@Autowired
	Mapper mapper;

	@GetMapping("/{id}")
	public PlayerDTO getById(@PathVariable Long id) {
		return mapper.toDTO(playerService.getById(id));
	}

	@GetMapping("")
	public List<PlayerDTO> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "desc") String direction) {
		return playerService.getAll(page, size, sort, direction)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
	}

	@GetMapping("/{id}/scores")
	public List<ScoreDTO> getScoresByPlayerId(@PathVariable Long id) {
		return playerService.getScoresByPlayerId(id)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public void deleteById(@PathVariable Long id) {
		playerService.deleteById(id);
	}

    @GetMapping("/{id}/principal")
    @PreAuthorize("isAuthenticated() && @permissionManager.hasPlayerPermission(authentication, #id)")
	public ResponseEntity<String> getPrincipalById(@PathVariable Long id) {
		return new ResponseEntity<>(playerService.getPrincipalById(id), HttpStatus.OK);
	}

}
