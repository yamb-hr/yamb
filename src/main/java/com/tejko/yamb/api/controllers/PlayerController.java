package com.tejko.yamb.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.payload.responses.PlayerResponse;
import com.tejko.yamb.api.payload.responses.PlayerStatsResponse;
import com.tejko.yamb.api.payload.responses.ScoreResponse;
import com.tejko.yamb.interfaces.services.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@GetMapping("/{id}")
	public PlayerResponse getById(@PathVariable Long id) {
		return playerService.getById(id);
	}

	@GetMapping("")
	public List<PlayerResponse> getAll() {
		return playerService.getAll();
	}

	@GetMapping("/{id}/scores")
	public List<ScoreResponse> getScoresByPlayerId(@PathVariable Long id) {
		return playerService.getScoresByPlayerId(id);
	}

	@GetMapping("/{id}/stats")
    public PlayerStatsResponse getPlayerStats(@PathVariable Long id) {
        return playerService.getPlayerStats(id);
    }

}
