package com.tejko.yamb.api.controllers;

import java.util.UUID;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.dto.requests.RelationshipRequest;
import com.tejko.yamb.api.dto.responses.FriendRequestResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.RelationshipResponse;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

    @Mapping("/block")
	public ResponseEntity<FriendRequestResponse> requestRelationshipByPlayerExternalId(@PathVariable UUID externalId, @RequestBody RelationshipRequest friendRequest) {
		EntityModel<FriendRequestResponse> relationshipResponse = playerModelAssembler.toModel(playerService.requestRelationshipByExternalId(externalId, relationshipRequest.getType()));
		return ResponseEntity.ok(relationshipResponse);
	}
    
	@PutMapping("/block")
	public ResponseEntity<FriendRequestResponse> requestRelationshipByPlayerExternalId(@PathVariable UUID externalId, @RequestBody RelationshipRequest friendRequest) {
		EntityModel<FriendRequestResponse> relationshipResponse = playerModelAssembler.toModel(playerService.requestRelationshipByExternalId(externalId, relationshipRequest.getType()));
		return ResponseEntity.ok(relationshipResponse);
	}

	@PutMapping("{externalId}/relationships/accept")
	public ResponseEntity<PlayerResponse> acceptRelationshipByPlayerExternalId(@PathVariable UUID externalId, @RequestBody RelationshipRequest friendRequest) {
		EntityModel<RelationshipResponse> relationshipResponse = playerModelAssembler.toModel(playerService.requestRelationshipByExternalId(externalId, relationshipRequest.getType()));
		return ResponseEntity.ok(relationshipResponse);
	}

	@PutMapping("{externalId}/relationships/decline")
	public ResponseEntity<PlayerResponse> declineRelationshipByPlayerExternalId(@PathVariable UUID externalId, @RequestBody RelationshipRequest friendRequest) {
		EntityModel<RelationshipResponse> relationshipResponse = playerModelAssembler.toModel(playerService.requestRelationshipByExternalId(externalId, relationshipRequest.getType()));
		return ResponseEntity.ok(relationshipResponse);
	}
    
}
