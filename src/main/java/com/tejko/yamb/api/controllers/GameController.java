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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.api.assemblers.GameModelAssembler;
import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.business.interfaces.GameService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.WebSocketMessage;
import com.tejko.yamb.util.SortFieldTranslator;



@RestController
@RequestMapping("/api/games")
public class GameController {

	private final GameService gameService;
	private final GameModelAssembler gameModelAssembler;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ObjectMapper objectMapper;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public GameController(GameService gameService, GameModelAssembler gameModelAssembler, SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper, SortFieldTranslator sortFieldTranslator) {
		this.gameService = gameService;
		this.gameModelAssembler = gameModelAssembler;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.objectMapper = objectMapper;
		this.sortFieldTranslator = sortFieldTranslator;
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
		WebSocketMessage message = new WebSocketMessage(objectMapper, MessageType.ROLL, gameResponse);
		simpMessagingTemplate.convertAndSend("/topic/games/" + gameResponse.getId(), message, message.getHeaders());
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/announce")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> announceByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.announceByExternalId(externalId,actionRequest.getBoxType()));
		WebSocketMessage message = new WebSocketMessage(objectMapper, MessageType.ANNOUNCE, gameResponse);
		simpMessagingTemplate.convertAndSend("/topic/games/" + gameResponse.getId(), message, message.getHeaders());
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/fill")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> fillByExternalId(@PathVariable UUID externalId, @Valid @RequestBody ActionRequest actionRequest) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.fillByExternalId(externalId,actionRequest.getColumnType(), actionRequest.getBoxType()));
		WebSocketMessage message = new WebSocketMessage(objectMapper, MessageType.ANNOUNCE, gameResponse);
		simpMessagingTemplate.convertAndSend("/topic/games/" + gameResponse.getId(), message, message.getHeaders());
		return ResponseEntity.ok(gameResponse);
	}

	@PutMapping("/{externalId}/restart")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<GameResponse> restartByExternalId(@PathVariable UUID externalId) {
		GameResponse gameResponse = gameModelAssembler.toModel(gameService.restartByExternalId(externalId));
		WebSocketMessage message = new WebSocketMessage(objectMapper, MessageType.ANNOUNCE, gameResponse);
		simpMessagingTemplate.convertAndSend("/topic/games/" + gameResponse.getId(), message, message.getHeaders());
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
			.location(linkTo(methodOn(GameController.class).getAll(null)).toUri())
			.build();
	}

}
