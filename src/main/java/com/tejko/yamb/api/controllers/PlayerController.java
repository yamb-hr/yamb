package com.tejko.yamb.api.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStats;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStats;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.domain.services.interfaces.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService playerService;

	@Autowired
	public PlayerController(PlayerService playerService) {
		this.playerService = playerService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(playerService.getById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<PlayerResponse>> getAll() {
		return ResponseEntity.ok(playerService.getAll());
	}

	@GetMapping("/stats")
    public ResponseEntity<GlobalPlayerStats> getGlobalStats() {
        return ResponseEntity.ok(playerService.getGlobalStats());
    }

	@GetMapping("/{id}/scores")
	public ResponseEntity<List<ScoreResponse>> getScoresByPlayerId(@PathVariable Long id) {
		return ResponseEntity.ok(playerService.getScoresByPlayerId(id));
	}

	@GetMapping("/{id}/stats")
    public ResponseEntity<PlayerStats> getPlayerStats(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerStats(id));
    }

	@GetMapping("{id}/preferences")
    public ResponseEntity<PlayerPreferencesResponse> getPreferencesByPlayerId(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPreferencesByPlayerId(id));
    }

	@PutMapping("{id}/preferences")
    public ResponseEntity<PlayerPreferencesResponse> setPreferencesByPlayerId(@PathVariable Long id, @Valid @RequestBody PlayerPreferencesRequest playerPreferencesRequest) {
        return ResponseEntity.ok(playerService.setPreferencesByPlayerId(id, playerPreferencesRequest));
    }

	@DeleteMapping("/inactive")
    public ResponseEntity<Void> deleteInactivePlayers() {
		playerService.deleteInactivePlayers();
    	return ResponseEntity.noContent().build();
	}
}
