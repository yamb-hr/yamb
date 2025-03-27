package com.tejko.yamb.api.controllers;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tejko.yamb.api.assemblers.ClashModelAssembler;
import com.tejko.yamb.api.assemblers.GameModelAssembler;
import com.tejko.yamb.api.assemblers.GlobalPlayerStatsModelAssembler;
import com.tejko.yamb.api.assemblers.LogModelAssembler;
import com.tejko.yamb.api.assemblers.NotificationModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerDetailModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerPreferencesModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerStatsModelAssembler;
import com.tejko.yamb.api.assemblers.RelationshipModelAssembler;
import com.tejko.yamb.api.assemblers.ScoreModelAssembler;
import com.tejko.yamb.api.dto.requests.EmailRequest;
import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.requests.UsernameRequest;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.api.dto.responses.NotificationResponse;
import com.tejko.yamb.api.dto.responses.PlayerDetailResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.RelationshipResponse;
import com.tejko.yamb.api.dto.responses.ScoreResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.util.SortFieldTranslator;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

	private final PlayerService playerService;
	private final PlayerModelAssembler playerModelAssembler;
	private final PlayerDetailModelAssembler playerDetailModelAssembler;
	private final ScoreModelAssembler scoreModelAssembler;
	private final GameModelAssembler gameModelAssembler;
	private final ClashModelAssembler clashModelAssembler;
	private final RelationshipModelAssembler relationshipModelAssembler;
	private final LogModelAssembler logModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;
	private final NotificationModelAssembler notificationModelAssembler;
	private final PlayerStatsModelAssembler playerStatsModelAssembler;
	private final PlayerPreferencesModelAssembler playerPreferencesModelAssembler;
	private final GlobalPlayerStatsModelAssembler globalPlayerStatsModelAssembler;

	@Autowired
	public PlayerController(PlayerService playerService, PlayerModelAssembler playerModelAssembler, 
							PlayerDetailModelAssembler playerDetailModelAssembler, ScoreModelAssembler scoreModelAssembler, 
							GameModelAssembler gameModelAssembler, ClashModelAssembler clashModelAssembler, 
							RelationshipModelAssembler relationshipModelAssembler, LogModelAssembler logModelAssembler, 
							SortFieldTranslator sortFieldTranslator, NotificationModelAssembler notificationModelAssembler,
							PlayerStatsModelAssembler playerStatsModelAssembler, PlayerPreferencesModelAssembler playerPreferencesModelAssembler, 
							GlobalPlayerStatsModelAssembler globalPlayerStatsModelAssembler) {
		this.playerService = playerService;
		this.playerModelAssembler = playerModelAssembler;
		this.playerDetailModelAssembler = playerDetailModelAssembler;
		this.scoreModelAssembler = scoreModelAssembler;
		this.gameModelAssembler = gameModelAssembler;
		this.clashModelAssembler = clashModelAssembler;
		this.relationshipModelAssembler = relationshipModelAssembler;
		this.logModelAssembler = logModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
		this.notificationModelAssembler = notificationModelAssembler;
		this.playerStatsModelAssembler = playerStatsModelAssembler;
		this.playerPreferencesModelAssembler = playerPreferencesModelAssembler;
		this.globalPlayerStatsModelAssembler = globalPlayerStatsModelAssembler;
	}

	@GetMapping("/{externalId}")
    public ResponseEntity<PlayerResponse> getByExternalId(@PathVariable UUID externalId) {
        PlayerResponse playerResponse = playerModelAssembler.toModel(playerService.getByExternalId(externalId));
        return ResponseEntity.ok(playerResponse);
    }

	@GetMapping("")
	public ResponseEntity<PagedModel<PlayerResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Player.class, PlayerResponse.class);
        PagedModel<PlayerResponse> pagedPlayers = playerModelAssembler.toPagedModel(playerService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedPlayers);
	}

	@GetMapping("/active")
	public ResponseEntity<CollectionModel<PlayerResponse>> getAllActive(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Player.class, PlayerResponse.class);
        CollectionModel<PlayerResponse> players = playerModelAssembler.toCollectionModel(playerService.getAllActive(modifiedPageable));
		return ResponseEntity.ok(players);
	}

	@GetMapping("/stats")
	@PreAuthorize("isAuthenticated()")
    public ResponseEntity<GlobalPlayerStatsResponse> getGlobalStats() {
		GlobalPlayerStatsResponse globalPlayerStatsResponse = globalPlayerStatsModelAssembler.toModel(playerService.getGlobalStats());
        return ResponseEntity.ok(globalPlayerStatsResponse);
    }

	@GetMapping("/{externalId}/scores")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<CollectionModel<ScoreResponse>> getScoresByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<ScoreResponse> scoreResponses = scoreModelAssembler.toCollectionModel(playerService.getScoresByPlayerExternalId(externalId));
		return ResponseEntity.ok(scoreResponses);
	}

	@GetMapping("/{externalId}/games")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<CollectionModel<GameResponse>> getGamesByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<GameResponse> gameResponses = gameModelAssembler.toCollectionModel(playerService.getGamesByPlayerExternalId(externalId));
		return ResponseEntity.ok(gameResponses);
	}

	@GetMapping("/{externalId}/clashes")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<CollectionModel<ClashResponse>> getClashesByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<ClashResponse> clashResponses = clashModelAssembler.toCollectionModel(playerService.getClashesByPlayerExternalId(externalId));
		return ResponseEntity.ok(clashResponses);
	}

	@GetMapping("/{externalId}/logs")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<CollectionModel<LogResponse>> getLogsByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<LogResponse> logResponses = logModelAssembler.toCollectionModel(playerService.getLogsByPlayerExternalId(externalId));
		return ResponseEntity.ok(logResponses);
	}

	@GetMapping("/{externalId}/stats")
	@PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerStatsResponse> getPlayerStatsByExternalId(@PathVariable UUID externalId) {
		PlayerStatsResponse playerStatsResponse = playerStatsModelAssembler.toModel(playerService.getPlayerStatsByExternalId(externalId));
        return ResponseEntity.ok(playerStatsResponse);
    }

	@GetMapping("/{externalId}/preferences")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<PlayerPreferencesResponse> getPreferencesByPlayerExternalId(@PathVariable UUID externalId) {
		PlayerPreferencesResponse playerPreferencesResponse = playerPreferencesModelAssembler.toModel(playerService.getPreferencesByPlayerExternalId(externalId));
        return ResponseEntity.ok(playerPreferencesResponse);
    }

	@PutMapping("/{externalId}/preferences")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<EntityModel<PlayerPreferencesResponse>> setPreferencesByPlayerExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerPreferencesRequest playerPreferencesRequest) {
		PlayerPreferencesResponse playerPreferencesResponse = playerPreferencesModelAssembler.toModel(playerService.setPreferencesByPlayerExternalId(externalId, playerPreferencesModelAssembler.fromModel(playerPreferencesRequest)));
		EntityModel<PlayerPreferencesResponse> preferencesModel = EntityModel.of(playerPreferencesResponse);
        return ResponseEntity.ok(preferencesModel);
    }

	@GetMapping("/{externalId}/relationships")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<CollectionModel<RelationshipResponse>> getRelationshipsByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<RelationshipResponse> relationshipResponses = relationshipModelAssembler.toCollectionModel(playerService.getRelationshipsByPlayerExternalId(externalId));
		return ResponseEntity.ok(relationshipResponses);
	}

	@DeleteMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteById(@PathVariable UUID externalId) {
		playerService.deleteByExternalId(externalId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
    public ResponseEntity<PlayerDetailResponse> getCurrentPlayer() {
		Player player = playerService.getCurrentPlayer();
		PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(player);
		return ResponseEntity.ok(playerDetailResponse);
	}

	@PutMapping("/{externalId}/username")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<PlayerDetailResponse> updateUsernameByExternalId(@PathVariable UUID externalId, @Valid @RequestBody UsernameRequest usernameRequest) {
		Player player = playerService.updateUsernameByExternalId(externalId, usernameRequest.getUsername());
		PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(player);
		return ResponseEntity.ok(playerDetailResponse);
	}

	@PutMapping("/{externalId}/email")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<PlayerDetailResponse> updateEmailByExternalId(@PathVariable UUID externalId, @Valid @RequestBody EmailRequest emailRequest) {
		Player player = playerService.updateEmailByExternalId(externalId, emailRequest.getEmail());
		PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(player);
		return ResponseEntity.ok(playerDetailResponse);
    }

	@PutMapping("/{externalId}/avatar")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<PlayerDetailResponse> updateAvatarByExternalId(@PathVariable UUID externalId, @RequestParam MultipartFile file) {
		Player player = playerService.updateAvatarByExternalId(externalId, file);
		PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(player);
		return ResponseEntity.ok(playerDetailResponse);
	}

	@GetMapping("/{externalId}/notifications")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<CollectionModel<NotificationResponse>> getNotificationsByPlayerExternalId(@PathVariable UUID externalId) {
		CollectionModel<NotificationResponse> notificationResponses = notificationModelAssembler.toCollectionModel(playerService.getNotificationsByPlayerExternalId(externalId));
		return ResponseEntity.ok(notificationResponses);
    }

	@DeleteMapping("/{externalId}/notifications")
	@PreAuthorize("isAuthenticated() and (#externalId == principal.externalId or hasAuthority('ADMIN'))")
    public ResponseEntity<Void> deleteNotificationsByPlayerExternalId(@PathVariable UUID externalId) {
		playerService.deleteNotificationsByPlayerExternalId(externalId);
		return ResponseEntity.noContent().build();
    }

}
