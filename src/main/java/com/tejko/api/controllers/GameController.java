package com.tejko.api.controllers;

import java.util.List;
import java.util.UUID;

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
import com.tejko.exceptions.IllegalMoveException;
import com.tejko.models.Game;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;

@RestController
@RequestMapping("/api/games")
public class GameController {

	@Autowired
	GameService gameService;

	@GetMapping("")
	public ResponseEntity<List<Game>> getAll(@RequestParam(defaultValue = "0") Integer page,
	@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort,
	@RequestParam(defaultValue = "desc") String direction) {
		return new ResponseEntity<>(gameService.getAll(page, size, sort, direction), HttpStatus.OK);
	}

	// @PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}") 
	public ResponseEntity<Game> getById(@PathVariable UUID id) {
		return new ResponseEntity<>(gameService.getById(id), HttpStatus.OK);
	}

	// @PreAuthorize("isAuthenticated()")
	@PostMapping("")
	public ResponseEntity<Game> create() {
		return new ResponseEntity<>(gameService.create(), HttpStatus.CREATED);
	}

	@PreAuthorize("@permissionComponent.hasPermission(#id)")
	@PutMapping("/{id}/roll")
	public ResponseEntity<Game> rollDiceById(@PathVariable UUID id, @RequestBody List<Integer> diceToRoll) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.rollDiceById(id, diceToRoll), HttpStatus.OK);
	}

	@PreAuthorize("hasPermission()")
	@PutMapping("/{id}/announce")
	public ResponseEntity<Game> announceById(@PathVariable UUID id, @RequestBody BoxType boxType) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.announceById(id, boxType), HttpStatus.OK);
	}

	@PreAuthorize("hasPermission()")
	@PutMapping("/{id}/columns/{columnType}/boxes/{boxType}/fill")
	public ResponseEntity<Game> fillById(@PathVariable UUID id, @PathVariable ColumnType columnType, @PathVariable BoxType boxType) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.fillById(id, columnType, boxType), HttpStatus.OK);
	}

	@PreAuthorize("hasPermission()")
	@PutMapping("/{id}/restart")
	public ResponseEntity<Game> restartById(@PathVariable UUID id) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.restartById(id), HttpStatus.OK);
	}

}
