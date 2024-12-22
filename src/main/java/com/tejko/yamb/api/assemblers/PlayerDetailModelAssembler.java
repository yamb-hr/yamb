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
import com.tejko.yamb.api.dto.responses.PlayerDetailResponse;
import com.tejko.yamb.domain.models.Player;

@Component
public class PlayerDetailModelAssembler implements RepresentationModelAssembler<Player, PlayerDetailResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public PlayerDetailModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PlayerDetailResponse toModel(Player player) {

        PlayerDetailResponse playerDetailResponse = modelMapper.map(player, PlayerDetailResponse.class);
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getByExternalId(playerDetailResponse.getId())).withSelfRel());
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getScoresByPlayerExternalId(playerDetailResponse.getId())).withRel("scores"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getGamesByPlayerExternalId(playerDetailResponse.getId())).withRel("games"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getClashesByPlayerExternalId(playerDetailResponse.getId())).withRel("clashes"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getLogsByPlayerExternalId(playerDetailResponse.getId())).withRel("logs"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getPreferencesByPlayerExternalId(playerDetailResponse.getId())).withRel("preferences"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).updateUsernameByExternalId(playerDetailResponse.getId(), null)).withRel("username"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).updateEmailByExternalId(playerDetailResponse.getId(), null)).withRel("email"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getPlayerStatsByExternalId(playerDetailResponse.getId())).withRel("stats"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).updateAvatarByExternalId(playerDetailResponse.getId(), null)).withRel("avatar"));
        playerDetailResponse.add(linkTo(methodOn(PlayerController.class).getNotificationsByPlayerExternalId(playerDetailResponse.getId())).withRel("notifications"));

        return playerDetailResponse;
    }

    public PagedModel<PlayerDetailResponse> toPagedModel(Page<Player> players) {
        List<PlayerDetailResponse> playerDetailResponses = players.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        return PagedModel.of(playerDetailResponses, new PagedModel.PageMetadata(
            players.getSize(), players.getNumber(), players.getTotalElements(), players.getTotalPages()
        ));
    }
    
}
