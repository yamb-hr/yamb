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
import com.tejko.yamb.api.assemblers.ClashModelAssembler;
import com.tejko.yamb.api.dto.requests.ClashRequest;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.business.interfaces.ClashService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.WebSocketMessage;



@RestController
@RequestMapping("/api/clashes")
public class ClashController {

	private final ClashService clashService;
	private final ClashModelAssembler clashModelAssembler;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ObjectMapper objectMapper;

	@Autowired
	public ClashController(ClashService clashService, ClashModelAssembler clashModelAssembler, SimpMessagingTemplate simpMessagingTemplate, ObjectMapper objectMapper) {
		this.clashService = clashService;
		this.clashModelAssembler = clashModelAssembler;
		this.simpMessagingTemplate = simpMessagingTemplate;
		this.objectMapper = objectMapper;
	}
	
	@GetMapping("/{externalId}")
	public ResponseEntity<ClashResponse> getByExternalId(@PathVariable UUID externalId) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.getByExternalId(externalId));
		return ResponseEntity.ok(clashResponse);
	}

	@GetMapping("")
	public ResponseEntity<PagedModel<ClashResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		PagedModel<ClashResponse> pagedClashs = clashModelAssembler.toPagedModel(clashService.getAll(pageable));
		return ResponseEntity.ok(pagedClashs);
	}

	@PutMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashResponse> getOrCreate(@Valid @RequestBody ClashRequest clashRequest) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.getOrCreate(clashRequest.getPlayerIds(), clashRequest.getType()));
		if (clashResponse.getCreatedAt().equals(clashResponse.getUpdatedAt())) {
			URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{externalId}")
				.buildAndExpand(clashResponse.getId())
				.toUri();
			return ResponseEntity.created(location).body(clashResponse);
		}

		return ResponseEntity.ok(clashResponse);
	}

	@PutMapping("/{externalId}/accept")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashResponse> acceptInvitationByExternalId(@PathVariable UUID externalId) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.acceptInvitationByExternalId(externalId));
		WebSocketMessage message = new WebSocketMessage(objectMapper, MessageType.ACCEPT, clashResponse);
		simpMessagingTemplate.convertAndSend("/topic/clashes/" + clashResponse.getId(), message, message.getHeaders());
		return ResponseEntity.ok(clashResponse);
	}

	@PutMapping("/{externalId}/decline")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashResponse> declineInvitationByExternalId(@PathVariable UUID externalId) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.declineInvitationByExternalId(externalId));
		WebSocketMessage message = new WebSocketMessage(objectMapper, MessageType.DECLINE, clashResponse);
		simpMessagingTemplate.convertAndSend("/topic/clashes/" + clashResponse.getId(), message, message.getHeaders());
		return ResponseEntity.ok(clashResponse);
	}
	
	@DeleteMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteByExternalId(@PathVariable UUID externalId) {
		clashService.deleteByExternalId(externalId);
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(ClashController.class).getAll(null)).toUri())
			.build();
	}

}
