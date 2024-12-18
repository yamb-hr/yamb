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

import com.tejko.yamb.api.assemblers.GameModelAssembler;
import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.business.interfaces.GameService;
import com.tejko.yamb.business.interfaces.WebSocketService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.util.SortFieldTranslator;



@RestController
@RequestMapping("/api/games")
public class GameController {

	private final GameService gameService;
	private final GameModelAssembler gameModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;
	private final WebSocketService webSocketService;

	@Autowired
	public GameController(GameService gameService, GameModelAssembler gameModelAssembler, SortFieldTranslator sortFieldTranslator, WebSocketService webSocketService) {
		this.gameService = gameService;
		this.gameModelAssembler = gameModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
		this.webSocketService = webSocketService;
	}
	
	@GetMapping("/{externalId}")
	public ResponseEntity<GameResponse> getByExternalId(@PathVariable UUID externalId) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.getByExternalId(externalId));
		return ResponseEntity.ok(gameResponse);
	}

	@GetMapping("")
	public ResponseEntity<PagedModel<GameResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Game.class, GameResponse.class);
		PagedModel<GameResponse> pagedGames = gameModelAssembler.toPagedModel(gameService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedGames);
	}

	@PutMapping("")
	@PreAuthorize("isAuthenticated() and (#gameRequest.playerId == principal.externalId or hasAuthority('ADMIN'))")
	public ResponseEntity<GameResponse> getOrCreate(@Valid @RequestBody GameRequest gameRequest) {
		
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.getOrCreate(gameRequest.getPlayerId()));
		if (gameResponse.getCreatedAt().equals(gameResponse.getUpdatedAt())) {
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{externalId}")
				.buildAndExpand(gameResponse.getId())
				.toUri();
			return ResponseEntity.created(location).body(gameResponse);
		}

		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/roll")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> rollByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.rollByExternalId(externalId,actionRequest.getDiceToRoll()));
		webSocketService.convertAndSend("/topic/games/" + gameResponse.getId(), gameResponse, MessageType.ROLL);
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/announce")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> announceByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.announceByExternalId(externalId,actionRequest.getBoxType()));
		webSocketService.convertAndSend("/topic/games/" + gameResponse.getId(), gameResponse, MessageType.ANNOUNCE);
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/fill")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> fillByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.fillByExternalId(externalId,actionRequest.getColumnType(), actionRequest.getBoxType()));
		webSocketService.convertAndSend("/topic/games/" + gameResponse.getId(), gameResponse, MessageType.FILL);
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/undo")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> undoFillByExternalId(@PathVariable UUID externalId) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.undoFillByExternalId(externalId));
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/restart")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> restartByExternalId(@PathVariable UUID externalId) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.restartByExternalId(externalId));
		webSocketService.convertAndSend("/topic/games/" + gameResponse.getId(), gameResponse, MessageType.RESTART);
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/archive")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> archiveByExternalId(@PathVariable UUID externalId) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.archiveByExternalId(externalId));
		return ResponseEntity.ok(gameResponse);
	}
	
	@PutMapping("/{externalId}/complete")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<GameResponse> completeByExternalId(@PathVariable UUID externalId) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.completeByExternalId(externalId));
		return ResponseEntity.ok(gameResponse);
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
