package com.tejko.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.api.services.GameService;
import com.tejko.models.Game;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.security.PermissionManager;

@RestController
@RequestMapping("/api/games")
public class GameController {

	@Autowired
	GameService gameService;

	@Autowired
	PermissionManager permissionManager;

	@GetMapping("")
	public ResponseEntity<List<Game>> getAll(@RequestParam(defaultValue = "0") Integer page,
	@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort,
	@RequestParam(defaultValue = "desc") String direction) {
		return new ResponseEntity<>(gameService.getAll(page, size, sort, direction), HttpStatus.OK);
	}

	@GetMapping("/{id}") 
	public ResponseEntity<Game> getById(@PathVariable Long id) {
		return new ResponseEntity<>(gameService.getById(id), HttpStatus.OK);
	}

	@PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Game> create() { 
		return new ResponseEntity<>(gameService.create(), HttpStatus.CREATED);
	}

	@PutMapping("/{id}/roll")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasPermission(authentication, #id)")
	public ResponseEntity<Game> rollDiceById(@PathVariable Long id, @RequestBody List<Integer> diceToRoll) {
		return new ResponseEntity<>(gameService.rollDiceById(id, diceToRoll), HttpStatus.OK);
	}

	@PutMapping("/{id}/announce")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasPermission(authentication, #id)")
	public ResponseEntity<Game> announceById(@PathVariable Long id, @RequestBody BoxType boxType) {
		return new ResponseEntity<>(gameService.announceById(id, boxType), HttpStatus.OK);
	}

	@PutMapping("/{id}/columns/{columnType}/boxes/{boxType}/fill")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasPermission(authentication, #id)")
	public ResponseEntity<Game> fillById(@PathVariable Long id, @PathVariable ColumnType columnType, @PathVariable BoxType boxType) {
		return new ResponseEntity<>(gameService.fillById(id, columnType, boxType), HttpStatus.OK);
	}

	@PutMapping("/{id}/restart")
	@PreAuthorize("isAuthenticated() && @permissionManager.hasPermission(authentication, #id)")
	public ResponseEntity<Game> restartById(@PathVariable Long id) {
		return new ResponseEntity<>(gameService.restartById(id), HttpStatus.OK);
	}

}
