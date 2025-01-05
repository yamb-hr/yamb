package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.controllers.RelationshipController;
import com.tejko.yamb.api.dto.responses.RelationshipResponse;
import com.tejko.yamb.domain.models.PlayerRelationship;

@Component
public class RelationshipModelAssembler implements RepresentationModelAssembler<PlayerRelationship, RelationshipResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public RelationshipModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RelationshipResponse toModel(PlayerRelationship relationship) {
        
        RelationshipResponse relationshipResponse = modelMapper.map(relationship, RelationshipResponse.class);
		relationshipResponse.add(linkTo(methodOn(RelationshipController.class).getByExternalId(relationshipResponse.getId())).withSelfRel());
		relationshipResponse.add(linkTo(methodOn(RelationshipController.class).acceptByExternalId(relationshipResponse.getId())).withRel("accept"));
		relationshipResponse.add(linkTo(methodOn(RelationshipController.class).declineByExternalId(relationshipResponse.getId())).withRel("decline"));
        if (relationshipResponse.getPlayer() != null) relationshipResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(relationshipResponse.getPlayer().getId())).withSelfRel());
        if (relationshipResponse.getRelatedPlayer() != null) relationshipResponse.getRelatedPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(relationshipResponse.getPlayer().getId())).withSelfRel());

        return relationshipResponse;
    }

    public PagedModel<RelationshipResponse> toPagedModel(Page<PlayerRelationship> relationships) {

        List<RelationshipResponse> relationshipResponses = relationships.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<RelationshipResponse> pagedRelationships = PagedModel.of(relationshipResponses, new PagedModel.PageMetadata(
            relationships.getSize(), relationships.getNumber(), relationships.getTotalElements(), relationships.getTotalPages()
        ));

        return pagedRelationships;
    }

}
