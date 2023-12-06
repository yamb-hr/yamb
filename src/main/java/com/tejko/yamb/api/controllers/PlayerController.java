package com.tejko.yamb.api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.PlayerService;
import com.tejko.yamb.models.Score;

import com.tejko.yamb.models.Player;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@GetMapping("/{id}")
	public ResponseEntity<Player> getById(@PathVariable Long id) {
		return new ResponseEntity<>(playerService.getById(id), HttpStatus.OK);
	}

	@GetMapping("")
	public ResponseEntity<List<Player>> getAll(@RequestParam(defaultValue = "0") Integer page,
	@RequestParam(defaultValue = "10") Integer size, @RequestParam(defaultValue = "id") String sort,
	@RequestParam(defaultValue = "desc") String direction) {
		return new ResponseEntity<>(playerService.getAll(page, size, sort, direction), HttpStatus.OK);
	}

	@GetMapping("/{id}/scores")
	public ResponseEntity<List<Score>> getScoresByPlayerId(@PathVariable Long id) {
		return new ResponseEntity<>(playerService.getScoresByPlayerId(id), HttpStatus.OK);
	}

}
