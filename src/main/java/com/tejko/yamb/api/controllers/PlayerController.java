package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.assemblers.ClashModelAssembler;
import com.tejko.yamb.api.assemblers.LogModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerModelAssembler;
import com.tejko.yamb.api.assemblers.ScoreModelAssembler;
import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.requests.UsernameRequest;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.business.interfaces.PlayerService;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService playerService;
	private final PlayerModelAssembler playerModelAssembler;
	private final ScoreModelAssembler scoreModelAssembler;
	private final ClashModelAssembler clashModelAssembler;
	private final LogModelAssembler logModelAssembler;

	@Autowired
	public PlayerController(PlayerService playerService, PlayerModelAssembler playerModelAssembler, ScoreModelAssembler scoreModelAssembler, ClashModelAssembler clashModelAssembler, LogModelAssembler logModelAssembler) {
		this.playerService = playerService;
		this.playerModelAssembler = playerModelAssembler;
		this.scoreModelAssembler = scoreModelAssembler;
		this.clashModelAssembler = clashModelAssembler;
		this.logModelAssembler = logModelAssembler;
	}

	@GetMapping("/{externalId}")
    public ResponseEntity<PlayerResponse> getByExternalId(@PathVariable UUID externalId) {
        PlayerResponse playerResponse = playerModelAssembler.toModel(playerService.getByExternalId(externalId));
        return ResponseEntity.ok(playerResponse);
    }

	@GetMapping("")
	public ResponseEntity<PagedModel<PlayerResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PagedModel<PlayerResponse> pagedPlayers = playerModelAssembler.toPagedModel(playerService.getAll(pageable));
		return ResponseEntity.ok(pagedPlayers);
	}

	@GetMapping("/stats")
    public ResponseEntity<GlobalPlayerStatsResponse> getGlobalStats() {
		GlobalPlayerStatsResponse globalPlayerStatsResponse = playerModelAssembler.toModel(playerService.getGlobalStats());
        return ResponseEntity.ok(globalPlayerStatsResponse);
    }

	@GetMapping("/{externalId}/scores")
	public ResponseEntity<CollectionModel<ScoreResponse>> getScoresByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<ScoreResponse> scoreResponses = scoreModelAssembler.toCollectionModel(playerService.getScoresByPlayerExternalId(externalId));
		return ResponseEntity.ok(scoreResponses);
	}

	@GetMapping("/{externalId}/clashes")
	public ResponseEntity<CollectionModel<ClashResponse>> getClashesByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<ClashResponse> clashResponses = clashModelAssembler.toCollectionModel(playerService.getClashesByPlayerExternalId(externalId));
		return ResponseEntity.ok(clashResponses);
	}

	@GetMapping("/{externalId}/logs")
	public ResponseEntity<CollectionModel<LogResponse>> getLogsByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<LogResponse> logResponses = logModelAssembler.toCollectionModel(playerService.getLogsByPlayerExternalId(externalId));
		return ResponseEntity.ok(logResponses);
	}

	@GetMapping("/{externalId}/stats")
    public ResponseEntity<PlayerStatsResponse> getPlayerStatsByExternalId(@PathVariable UUID externalId) {
		PlayerStatsResponse playerStatsResponse = playerModelAssembler.toModel(playerService.getPlayerStatsByExternalId(externalId));
        return ResponseEntity.ok(playerStatsResponse);
    }

	@GetMapping("{externalId}/preferences")
    public ResponseEntity<PlayerPreferencesResponse> getPreferencesByPlayerExternalId(@PathVariable UUID externalId) {
		PlayerPreferencesResponse playerPreferencesResponse = playerModelAssembler.toModel(playerService.getPreferencesByPlayerExternalId(externalId));
        return ResponseEntity.ok(playerPreferencesResponse);
    }

	@PutMapping("{externalId}/preferences")
    public ResponseEntity<EntityModel<PlayerPreferencesResponse>> setPreferencesByPlayerExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerPreferencesRequest playerPreferencesRequest) {
		PlayerPreferencesResponse playerPreferencesResponse = playerModelAssembler.toModel(playerService.setPreferencesByPlayerExternalId(externalId, playerModelAssembler.fromModel(playerPreferencesRequest)));

		EntityModel<PlayerPreferencesResponse> preferencesModel = EntityModel.of(playerPreferencesResponse);
        preferencesModel.add(linkTo(methodOn(PlayerController.class).setPreferencesByPlayerExternalId(externalId, playerPreferencesRequest)).withSelfRel());
        preferencesModel.add(linkTo(methodOn(PlayerController.class).getByExternalId(externalId)).withRel("player"));

        return ResponseEntity.ok(preferencesModel);
    }

	@PutMapping("{externalId}/username")
	public ResponseEntity<PlayerResponse> changeUsernameByExternalId(@PathVariable UUID externalId, @RequestBody UsernameRequest usernameRequest) {
		PlayerResponse playerResponse = playerModelAssembler.toModel(playerService.changeUsernameByExternalId(externalId, usernameRequest.getUsername()));
		return ResponseEntity.ok(playerResponse);
	}

	@DeleteMapping("/inactive")
    public ResponseEntity<Void> deleteInactivePlayers() {
		playerService.deleteInactivePlayers();
    	return ResponseEntity.noContent().build();
	}

	@GetMapping("/me")
    public ResponseEntity<PlayerResponse> getCurrentPlayer() {
		PlayerResponse playerResponse = playerModelAssembler.toModel(playerService.getCurrentPlayer());
		return ResponseEntity.ok(playerResponse);
	}
}
