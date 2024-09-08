package com.tejko.yamb.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.ShortScoreResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.entities.PlayerPreferences;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService playerService;
	private final ModelMapper modelMapper;

	@Autowired
	public PlayerController(PlayerService playerService, ModelMapper modelMapper) {
		this.playerService = playerService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlayerResponse> getById(@PathVariable Long id) {
		PlayerResponse playerResponse = modelMapper.map(playerService.getById(id), PlayerResponse.class);
		return ResponseEntity.ok(playerResponse);
	}

	@GetMapping("")
	public ResponseEntity<List<PlayerResponse>> getAll() {
		List<PlayerResponse> playerResponses = playerService.getAll().stream().map(player -> modelMapper.map(player, PlayerResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(playerResponses);
	}

	@GetMapping("/stats")
    public ResponseEntity<GlobalPlayerStatsResponse> getGlobalStats() {
		GlobalPlayerStatsResponse globalPlayerStatsResponse = modelMapper.map(playerService.getGlobalStats(), GlobalPlayerStatsResponse.class);
        return ResponseEntity.ok(globalPlayerStatsResponse);
    }

	@GetMapping("/{id}/scores")
	public ResponseEntity<List<ShortScoreResponse>> getScoresByPlayerId(@PathVariable Long id) {
		List<ShortScoreResponse> scoreResponses = playerService.getScoresByPlayerId(id).stream().map(score -> modelMapper.map(score, ShortScoreResponse.class)).collect(Collectors.toList());
		return ResponseEntity.ok(scoreResponses);
	}

	@GetMapping("/{id}/stats")
    public ResponseEntity<PlayerStatsResponse> getPlayerStats(@PathVariable Long id) {
		PlayerStatsResponse playerStatsResponse = modelMapper.map(playerService.getPlayerStats(id), PlayerStatsResponse.class);
        return ResponseEntity.ok(playerStatsResponse);
    }

	@GetMapping("{id}/preferences")
    public ResponseEntity<PlayerPreferencesResponse> getPreferencesByPlayerId(@PathVariable Long id) {
		PlayerPreferencesResponse playerPreferencesResponse = modelMapper.map(playerService.getPreferencesByPlayerId(id), PlayerPreferencesResponse.class);
        return ResponseEntity.ok(playerPreferencesResponse);
    }

	@PutMapping("{id}/preferences")
    public ResponseEntity<PlayerPreferencesResponse> setPreferencesByPlayerId(@PathVariable Long id, @Valid @RequestBody PlayerPreferencesRequest playerPreferencesRequest) {
		PlayerPreferencesResponse playerPreferencesResponse = modelMapper.map(playerService.setPreferencesByPlayerId(id, modelMapper.map(playerPreferencesRequest, PlayerPreferences.class)), PlayerPreferencesResponse.class);
        return ResponseEntity.ok(playerPreferencesResponse);
    }

	@PutMapping("{id}/username")
	public ResponseEntity<PlayerResponse> changeUsername(@PathVariable Long id, @RequestBody String username) {
		PlayerResponse playerResponse = modelMapper.map(playerService.changeUsername(id, username), PlayerResponse.class);
		return ResponseEntity.ok(playerResponse);
	}

	@DeleteMapping("/inactive")
    public ResponseEntity<Void> deleteInactivePlayers() {
		playerService.deleteInactivePlayers();
    	return ResponseEntity.noContent().build();
	}
}
