package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.GameDetailModelAssembler;
import com.tejko.yamb.api.assemblers.GameModelAssembler;
import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameDetailResponse;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.business.interfaces.GameService;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.util.SortFieldTranslator;



@RestController
@RequestMapping("/api/games")
public class GameController {

	private final GameService gameService;
	private final GameModelAssembler gameModelAssembler;
	private final GameDetailModelAssembler gameDetailModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public GameController(GameService gameService, GameModelAssembler gameModelAssembler, 
						  GameDetailModelAssembler gameDetailModelAssembler, SortFieldTranslator sortFieldTranslator) {
		this.gameService = gameService;
		this.gameModelAssembler = gameModelAssembler;
		this.gameDetailModelAssembler = gameDetailModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}
	
	@GetMapping("/{externalId}")
	public ResponseEntity<GameDetailResponse> getByExternalId(@PathVariable UUID externalId) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.getByExternalId(externalId));
		return ResponseEntity.ok(gameDetailResponse);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<GameResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Game.class, GameResponse.class);
		PagedModel<GameResponse> pagedGames = gameModelAssembler.toPagedModel(gameService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedGames);
	}

	@PutMapping("")
	@PreAuthorize("isAuthenticated() and (#gameRequest.playerId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<GameDetailResponse> getOrCreate(@Valid @RequestBody GameRequest gameRequest) {
		
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.getOrCreate(gameRequest.getPlayerId()));
		if (gameDetailResponse.getCreatedAt().equals(gameDetailResponse.getUpdatedAt())) {
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{externalId}")
				.buildAndExpand(gameDetailResponse.getId())
				.toUri();
			return ResponseEntity.created(location).body(gameDetailResponse);
		}

		return ResponseEntity.ok(gameDetailResponse);
	}

	@PutMapping("/{externalId}/roll")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameDetailResponse> rollByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.rollByExternalId(externalId,actionRequest.getDiceToRoll()));
		return ResponseEntity.ok(gameDetailResponse);
	}

	@PutMapping("/{externalId}/announce")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameDetailResponse> announceByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.announceByExternalId(externalId,actionRequest.getBoxType()));
		return ResponseEntity.ok(gameDetailResponse);
	}

	@PutMapping("/{externalId}/fill")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameDetailResponse> fillByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.fillByExternalId(externalId,actionRequest.getColumnType(), actionRequest.getBoxType()));
		return ResponseEntity.ok(gameDetailResponse);
	}

	@PutMapping("/{externalId}/undo")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameDetailResponse> undoFillByExternalId(@PathVariable UUID externalId) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.undoFillByExternalId(externalId));
		return ResponseEntity.ok(gameDetailResponse);
	}

	@PutMapping("/{externalId}/restart")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameDetailResponse> restartByExternalId(@PathVariable UUID externalId) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.restartByExternalId(externalId));
		return ResponseEntity.ok(gameDetailResponse);
	}

	@PutMapping("/{externalId}/archive")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameDetailResponse> archiveByExternalId(@PathVariable UUID externalId) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.archiveByExternalId(externalId));
		return ResponseEntity.ok(gameDetailResponse);
	}
	
	@PutMapping("/{externalId}/complete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<GameDetailResponse> completeByExternalId(@PathVariable UUID externalId) {
		GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(gameService.completeByExternalId(externalId));
		return ResponseEntity.ok(gameDetailResponse);
	}
	
	@DeleteMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteByExternalId(@PathVariable UUID externalId) {
		gameService.deleteByExternalId(externalId);
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(GameController.class).getAll(Pageable.unpaged())).toUri())
			.build();
	}
	
	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		gameService.deleteAll();
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(GameController.class).getAll(Pageable.unpaged())).toUri())
			.build();
	}

}
