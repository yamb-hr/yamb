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
import org.springframework.web.bind.annotation.RestController;

import com.tejko.api.services.GameService;
import com.tejko.exceptions.IllegalMoveException;
import com.tejko.models.Game;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.models.payload.GameResponse;

@RestController
@RequestMapping("/api/games")
public class GameController {

	@Autowired
	GameService gameService;

	// @PreAuthorize("isAuthenticated()")
	@GetMapping("/{id}") 
	public ResponseEntity<GameResponse> getById(@PathVariable UUID id) {
		return new ResponseEntity<>(gameService.getById(id), HttpStatus.OK);
	}

	@GetMapping("/other/{id}") 
	public ResponseEntity<Game> getOtherById(@PathVariable UUID id) {
		return new ResponseEntity<>(gameService.getOtherById(id), HttpStatus.OK);
	}

	// @PreAuthorize("isAuthenticated()")
	@PostMapping("")
	public ResponseEntity<GameResponse> create() {
		return new ResponseEntity<>(gameService.create(), HttpStatus.CREATED);
	}

	@PreAuthorize("@permissionComponent.hasPermission(#id)")
	@PutMapping("/{id}/roll")
	public ResponseEntity<GameResponse> rollDiceById(@PathVariable UUID id, @RequestBody List<Integer> diceToRoll) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.rollDiceById(id, diceToRoll), HttpStatus.OK);
	}

	@PreAuthorize("hasPermission()")
	@PutMapping("/{id}/announce")
	public ResponseEntity<GameResponse> announceById(@PathVariable UUID id, @RequestBody BoxType boxType) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.announceById(id, boxType), HttpStatus.OK);
	}

	@PreAuthorize("hasPermission()")
	@PutMapping("/{id}/columns/{columnType}/boxes/{boxType}/fill")
	public ResponseEntity<GameResponse> fillById(@PathVariable UUID id, @PathVariable ColumnType columnType, @PathVariable BoxType boxType) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.fillById(id, columnType, boxType), HttpStatus.OK);
	}

	@PreAuthorize("hasPermission()")
	@PutMapping("/{id}/restart")
	public ResponseEntity<GameResponse> restartById(@PathVariable UUID id) throws IllegalMoveException {
		return new ResponseEntity<>(gameService.restartById(id), HttpStatus.OK);
	}

}
