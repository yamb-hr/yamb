package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.ClashController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.ClashPlayerResponse;
import com.tejko.yamb.api.dto.responses.ClashResponse;
import com.tejko.yamb.domain.models.Clash;

import java.util.List;

@Component
public class ClashModelAssembler implements RepresentationModelAssembler<Clash, ClashResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public ClashModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ClashResponse toModel(Clash clash) {
        
        ClashResponse clashResponse = modelMapper.map(clash, ClashResponse.class);
        clashResponse.add(linkTo(methodOn(ClashController.class).getByExternalId(clashResponse.getId())).withSelfRel());
        if (clashResponse.getOwner() != null) clashResponse.getOwner().add(linkTo(methodOn(PlayerController.class).getByExternalId(clashResponse.getOwner().getId())).withSelfRel());
        if (clashResponse.getWinner() != null) clashResponse.getWinner().add(linkTo(methodOn(PlayerController.class).getByExternalId(clashResponse.getWinner().getId())).withSelfRel());
        if (clashResponse.getPlayers() != null && !clashResponse.getPlayers().isEmpty()) {
            for (ClashPlayerResponse playerResponse : clashResponse.getPlayers()) {
                if (playerResponse != null) playerResponse.add(linkTo(methodOn(PlayerController.class).getByExternalId(playerResponse.getId())).withSelfRel());
            }
        }

        return clashResponse;
    }

    public PagedModel<ClashResponse> toPagedModel(Page<Clash> clashes) {

        List<ClashResponse> clashResponses = clashes.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<ClashResponse> pagedClashs = PagedModel.of(clashResponses, new PagedModel.PageMetadata(
            clashes.getSize(), clashes.getNumber(), clashes.getTotalElements(), clashes.getTotalPages()
        ));

        return pagedClashs;
    }
}
