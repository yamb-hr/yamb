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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.ClashModelAssembler;
import com.tejko.yamb.api.dto.requests.ClashRequest;
import com.tejko.yamb.api.dto.requests.PlayerIdRequest;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.business.interfaces.ClashService;
import com.tejko.yamb.business.interfaces.WebSocketService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.util.SortFieldTranslator;



@RestController
@RequestMapping("/api/clashes")
public class ClashController {

	private final ClashService clashService;
	private final ClashModelAssembler clashModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;
	private final WebSocketService webSocketService;

	@Autowired
	public ClashController(ClashService clashService, ClashModelAssembler clashModelAssembler, SortFieldTranslator sortFieldTranslator, WebSocketService webSocketService) {
		this.clashService = clashService;
		this.clashModelAssembler = clashModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
		this.webSocketService = webSocketService;
	}
	
	@GetMapping("/{externalId}")
	public ResponseEntity<ClashResponse> getByExternalId(@PathVariable UUID externalId) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.getByExternalId(externalId));
		return ResponseEntity.ok(clashResponse);
	}

	@GetMapping("")
	public ResponseEntity<PagedModel<ClashResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Clash.class, ClashResponse.class);
		PagedModel<ClashResponse> pagedClashs = clashModelAssembler.toPagedModel(clashService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedClashs);
	}

	@PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashResponse> create(@Valid @RequestBody ClashRequest clashRequest) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.create(clashRequest.getName(), clashRequest.getOwnerId(), clashRequest.getPlayerIds(), clashRequest.getType()));
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{externalId}")
			.buildAndExpand(clashResponse.getId())
			.toUri();
		return ResponseEntity.created(location).body(clashResponse);
	}

	@PutMapping("/{externalId}/accept")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashResponse> acceptInvitationByExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerIdRequest playerIdRequest) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.acceptInvitationByExternalId(externalId, playerIdRequest.getPlayerId()));
		webSocketService.convertAndSend("/topic/clashes/" + clashResponse.getId(), clashResponse, MessageType.ACCEPT);
		return ResponseEntity.ok(clashResponse);
	}

	@PutMapping("/{externalId}/decline")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashResponse> declineInvitationByExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerIdRequest playerIdRequest) {
		ClashResponse clashResponse = clashModelAssembler.toModel(clashService.declineInvitationByExternalId(externalId, playerIdRequest.getPlayerId()));
		webSocketService.convertAndSend("/topic/clashes/" + clashResponse.getId(), clashResponse, MessageType.DECLINE);
		return ResponseEntity.ok(clashResponse);
	}
	
	@DeleteMapping("/{externalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteByExternalId(@PathVariable UUID externalId) {
		clashService.deleteByExternalId(externalId);
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(ClashController.class).getAll(Pageable.unpaged())).toUri())
			.build();
	}

	@DeleteMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deleteAll() {
		clashService.deleteAll();
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(ClashController.class).getAll(Pageable.unpaged())).toUri())
			.build();
	}

}
