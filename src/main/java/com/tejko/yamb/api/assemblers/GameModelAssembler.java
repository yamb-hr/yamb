package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.GameController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.domain.models.Game;

import java.util.List;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, GameResponse> {

    private final ModelMapper modelMapper;

    public GameModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public GameResponse toModel(Game game) {
        
        GameResponse gameResponse = modelMapper.map(game, GameResponse.class);
        gameResponse.add(linkTo(methodOn(GameController.class).getByExternalId(gameResponse.getId())).withSelfRel());
        gameResponse.add(linkTo(methodOn(GameController.class).rollByExternalId(gameResponse.getId(), null)).withRel("roll"));
        gameResponse.add(linkTo(methodOn(GameController.class).announceByExternalId(gameResponse.getId(), null)).withRel("announce"));
        gameResponse.add(linkTo(methodOn(GameController.class).fillByExternalId(gameResponse.getId(), null)).withRel("fill"));
        gameResponse.add(linkTo(methodOn(GameController.class).restartByExternalId(gameResponse.getId())).withRel("restart"));
        gameResponse.add(linkTo(methodOn(GameController.class).archiveByExternalId(gameResponse.getId())).withRel("archive"));
        gameResponse.add(linkTo(methodOn(GameController.class).completeByExternalId(gameResponse.getId())).withRel("complete"));
        gameResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(gameResponse.getPlayer().getId())).withSelfRel());

        return gameResponse;
    }

    public PagedModel<GameResponse> toPagedModel(Page<Game> games) {

        List<GameResponse> gameResponses = games.stream()
            .map(this::toModel)
            .collect(Collectors.toList());

        PagedModel<GameResponse> pagedGames = PagedModel.of(gameResponses, new PagedModel.PageMetadata(
            games.getSize(), games.getNumber(), games.getTotalElements(), games.getTotalPages()
        ));

        return pagedGames;
    }
}
