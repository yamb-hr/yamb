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
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.domain.models.Player;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, PlayerResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public PlayerModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PlayerResponse toModel(Player player) {

        PlayerResponse playerResponse = modelMapper.map(player, PlayerResponse.class);
        playerResponse.add(linkTo(methodOn(PlayerController.class).getByExternalId(playerResponse.getId())).withSelfRel());
        playerResponse.add(linkTo(methodOn(PlayerController.class).getScoresByPlayerExternalId(playerResponse.getId())).withRel("scores"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getGamesByPlayerExternalId(playerResponse.getId())).withRel("games"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getClashesByPlayerExternalId(playerResponse.getId())).withRel("clashes"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getLogsByPlayerExternalId(playerResponse.getId())).withRel("logs"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getPreferencesByPlayerExternalId(playerResponse.getId())).withRel("preferences"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getPlayerStatsByExternalId(playerResponse.getId())).withRel("stats"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getNotificationsByPlayerExternalId(playerResponse.getId())).withRel("notifications"));

        return playerResponse;
    }

    public PagedModel<PlayerResponse> toPagedModel(Page<Player> players) {
        List<PlayerResponse> playerResponses = players.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return PagedModel.of(playerResponses, new PagedModel.PageMetadata(
            players.getSize(), players.getNumber(), players.getTotalElements(), players.getTotalPages()
        ));
    }

}
