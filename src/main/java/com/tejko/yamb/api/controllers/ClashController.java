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

import com.tejko.yamb.api.assemblers.ClashDetailModelAssembler;
import com.tejko.yamb.api.assemblers.ClashModelAssembler;
import com.tejko.yamb.api.dto.requests.ClashRequest;
import com.tejko.yamb.api.dto.requests.PlayerIdRequest;
import com.tejko.yamb.api.dto.requests.PlayerIdSetRequest;
import com.tejko.yamb.api.dto.responses.ClashDetailResponse;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.business.interfaces.ClashService;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.util.SortFieldTranslator;



@RestController
@RequestMapping("/api/clashes")
public class ClashController {

	private final ClashService clashService;
	private final ClashModelAssembler clashModelAssembler;
	private final ClashDetailModelAssembler clashDetailModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public ClashController(ClashService clashService, ClashModelAssembler clashModelAssembler, 
						   ClashDetailModelAssembler clashDetailModelAssembler, SortFieldTranslator sortFieldTranslator) {
		this.clashService = clashService;
		this.clashModelAssembler = clashModelAssembler;
		this.clashDetailModelAssembler = clashDetailModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}
	
	@GetMapping("/{externalId}")
	public ResponseEntity<ClashDetailResponse> getByExternalId(@PathVariable UUID externalId) {
		ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(clashService.getByExternalId(externalId));
		return ResponseEntity.ok(clashDetailResponse);
	}

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<ClashResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, Clash.class, ClashResponse.class);
		PagedModel<ClashResponse> pagedClashs = clashModelAssembler.toPagedModel(clashService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedClashs);
	}

	@PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashDetailResponse> create(@Valid @RequestBody ClashRequest clashRequest) {
		ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(clashService.create(clashRequest.getName(), clashRequest.getOwnerId(), clashRequest.getPlayerIds(), clashRequest.getType()));
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{externalId}")
			.buildAndExpand(clashDetailResponse.getId())
			.toUri();
		return ResponseEntity.created(location).body(clashDetailResponse);
	}

	@PutMapping("/{externalId}/accept")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashDetailResponse> acceptInvitationByExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerIdRequest playerIdRequest) {
		ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(clashService.acceptInvitationByExternalId(externalId, playerIdRequest.getPlayerId()));
		return ResponseEntity.ok(clashDetailResponse);
	}

	@PutMapping("/{externalId}/decline")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashDetailResponse> declineInvitationByExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerIdRequest playerIdRequest) {
		ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(clashService.declineInvitationByExternalId(externalId, playerIdRequest.getPlayerId()));
		return ResponseEntity.ok(clashDetailResponse);
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

	@PutMapping("/{externalId}/players/add")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashDetailResponse> addPlayersByExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerIdSetRequest playerIdSetRequest) {
		ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(clashService.addPlayersByExternalId(externalId, playerIdSetRequest.getPlayerIds()));
		return ResponseEntity.ok(clashDetailResponse);
	}

	@PutMapping("/{externalId}/players/remove")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ClashDetailResponse> removePlayersByExternalId(@PathVariable UUID externalId, @Valid @RequestBody PlayerIdSetRequest playerIdSetRequest) {
		ClashDetailResponse clashDetailResponse = clashDetailModelAssembler.toModel(clashService.removePlayersByExternalId(externalId, playerIdSetRequest.getPlayerIds()));
		return ResponseEntity.ok(clashDetailResponse);
	}


}
