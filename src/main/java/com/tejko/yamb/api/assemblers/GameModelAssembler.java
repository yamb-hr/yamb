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
        gameResponse.add(linkTo(methodOn(GameController.class).getById(gameResponse.getId())).withSelfRel());
        gameResponse.add(linkTo(methodOn(GameController.class).rollById(gameResponse.getId(), null)).withRel("roll"));
        gameResponse.add(linkTo(methodOn(GameController.class).announceById(gameResponse.getId(), null)).withRel("announce"));
        gameResponse.add(linkTo(methodOn(GameController.class).fillById(gameResponse.getId(), null)).withRel("fill"));
        gameResponse.add(linkTo(methodOn(GameController.class).restartById(gameResponse.getId())).withRel("restart"));
        gameResponse.add(linkTo(methodOn(GameController.class).archiveById(gameResponse.getId())).withRel("archive"));
        gameResponse.add(linkTo(methodOn(GameController.class).completeById(gameResponse.getId())).withRel("complete"));
        gameResponse.add(linkTo(methodOn(PlayerController.class).getByExternalId(gameResponse.getPlayerId())).withRel("player"));

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
