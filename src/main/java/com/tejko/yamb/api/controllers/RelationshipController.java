package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.tejko.yamb.api.assemblers.RelationshipModelAssembler;
import com.tejko.yamb.api.dto.requests.RelationshipRequest;
import com.tejko.yamb.api.dto.responses.RelationshipResponse;
import com.tejko.yamb.business.interfaces.RelationshipService;
import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.util.SortFieldTranslator;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

	private final RelationshipService relationshipService;
	private final RelationshipModelAssembler relationshipModelAssembler;
	private final SortFieldTranslator sortFieldTranslator;

	@Autowired
	public RelationshipController(RelationshipService relationshipService, RelationshipModelAssembler relationshipModelAssembler, SortFieldTranslator sortFieldTranslator) {
		this.relationshipService = relationshipService;
		this.relationshipModelAssembler = relationshipModelAssembler;
		this.sortFieldTranslator = sortFieldTranslator;
	}

	@GetMapping("/{externalId}")
    public ResponseEntity<RelationshipResponse> getByExternalId(@PathVariable UUID externalId) {
        RelationshipResponse relationshipResponse = relationshipModelAssembler.toModel(relationshipService.getByExternalId(externalId));
        return ResponseEntity.ok(relationshipResponse);
    }

	@GetMapping("")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<PagedModel<RelationshipResponse>> getAll(@PageableDefault(page = 0, size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Pageable modifiedPageable = sortFieldTranslator.translateSortField(pageable, PlayerRelationship.class, RelationshipResponse.class);
		PagedModel<RelationshipResponse> pagedRelationships = relationshipModelAssembler.toPagedModel(relationshipService.getAll(modifiedPageable));
		return ResponseEntity.ok(pagedRelationships);
	}


    @PostMapping("")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<RelationshipResponse> requestRelationship(@Valid @RequestBody RelationshipRequest relationshipRequest) {
		RelationshipResponse relationshipResponse = relationshipModelAssembler.toModel(relationshipService.requestRelationship(relationshipRequest.getPlayerId(), relationshipRequest.getRelatedPlayerId(), relationshipRequest.getType()));
		return ResponseEntity.ok(relationshipResponse);
	}

	@PutMapping("/{externalId}/accept")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<RelationshipResponse> acceptByExternalId(@PathVariable UUID externalId) {
		RelationshipResponse relationshipResponse = relationshipModelAssembler.toModel(relationshipService.acceptByExternalId(externalId));
		return ResponseEntity.ok(relationshipResponse);
	}

	@PutMapping("/{externalId}/decline")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> declineByExternalId(@PathVariable UUID externalId) {
		relationshipService.declineByExternalId(externalId);
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(RelationshipController.class).getAll(null)).toUri())
			.build();
	}

	@DeleteMapping("/relationships/{externalId}")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> deleteByExternalId(@PathVariable UUID externalId) {
		relationshipService.deleteByExternalId(externalId);
		return ResponseEntity.noContent()
			.location(linkTo(methodOn(RelationshipController.class).getAll(null)).toUri())
			.build();
	}
    
}
