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
import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.domain.models.GlobalPlayerStats;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerStats;

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
        playerResponse.add(linkTo(methodOn(PlayerController.class).updateUsernameByExternalId(playerResponse.getId(), null)).withRel("username"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).updateEmailByExternalId(playerResponse.getId(), null)).withRel("email"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).getPlayerStatsByExternalId(playerResponse.getId())).withRel("stats"));
        playerResponse.add(linkTo(methodOn(PlayerController.class).updateAvatarByExternalId(playerResponse.getId(), null)).withRel("avatar"));

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

    public GlobalPlayerStatsResponse toModel(GlobalPlayerStats globalPlayerStats) {
        GlobalPlayerStatsResponse globalPlayerStatsResponse = modelMapper.map(globalPlayerStats, GlobalPlayerStatsResponse.class);
        return globalPlayerStatsResponse;

    }

    public PlayerStatsResponse toModel(PlayerStats PlayerStats) {
        PlayerStatsResponse playerStatsResponse = modelMapper.map(PlayerStats, PlayerStatsResponse.class);
        return playerStatsResponse;
    }

    public PlayerPreferencesResponse toModel(PlayerPreferences playerPreferences) {
        PlayerPreferencesResponse playerPreferencesResponse = modelMapper.map(playerPreferences, PlayerPreferencesResponse.class);
        return playerPreferencesResponse;
    }

    public PlayerPreferences fromModel(PlayerPreferencesRequest playerPreferencesRequest) {
        PlayerPreferences playerPreferences = modelMapper.map(playerPreferencesRequest, PlayerPreferences.class);
        return playerPreferences;
    }
}
