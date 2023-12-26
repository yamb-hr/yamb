package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.GameService;
import com.tejko.yamb.models.enums.BoxType;
import com.tejko.yamb.models.payload.GameAction;
import com.tejko.yamb.models.payload.dto.DiceDTO;
import com.tejko.yamb.models.payload.dto.GameDTO;
import com.tejko.yamb.util.Mapper;

@RestController
@RequestMapping("/api/games")
public class GameController {

	@Autowired
	GameService gameService;

	@Autowired
	Mapper mapper;

	@GetMapping("")
	public List<GameDTO> getAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort, @RequestParam(defaultValue = "desc") String direction) {
		return gameService.getAll(page, size, sort, direction)
			.stream()
			.map(mapper::toDTO)
			.collect(Collectors.toList());
	}

	@GetMapping("/{id}") 
	public GameDTO getById(@PathVariable Long id) {
		return mapper.toDTO(gameService.getById(id));
	}

	@PostMapping("/play")
	@PreAuthorize("isAuthenticated()")
	public GameDTO play() { 
		return mapper.toDTO(gameService.play());
	}

	@PutMapping("/{id}/roll")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public List<DiceDTO> rollDiceById(@PathVariable Long id, @RequestBody GameAction action) {
		return gameService.rollDiceById(id, action)
				.stream()
				.map(mapper::toDTO)
				.collect(Collectors.toList());
	}

	@PutMapping("/{id}/announce")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public BoxType makeAnnouncementById(@PathVariable Long id, @RequestBody GameAction action) {
		return gameService.makeAnnouncementById(id, action);
	}

	@PutMapping("/{id}/fill")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public int fillBoxById(@PathVariable Long id, @RequestBody GameAction action) {
		return gameService.fillBoxById(id, action);
	}

	@PutMapping("/{id}/restart")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasGamePermission(authentication, #id)")
	public GameDTO restartById(@PathVariable Long id) {
		return mapper.toDTO(gameService.restartById(id));
	}

}
