package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.GameController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.GameDetailResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Game;

@Component
public class GameDetailModelAssembler implements RepresentationModelAssembler<Game, GameDetailResponse> {

    private final ModelMapper modelMapper;
    private final PlayerService playerService;

    @Autowired
    public GameDetailModelAssembler(ModelMapper modelMapper, PlayerService playerService) {
        this.modelMapper = modelMapper;
        this.playerService = playerService;
    }

    @Override
    public GameDetailResponse toModel(Game game) {
        
        GameDetailResponse gameDetailResponse = modelMapper.map(game, GameDetailResponse.class);
        gameDetailResponse.add(linkTo(methodOn(GameController.class).getByExternalId(gameDetailResponse.getId())).withSelfRel());
        gameDetailResponse.add(linkTo(methodOn(GameController.class).rollByExternalId(gameDetailResponse.getId(), null)).withRel("roll"));
        gameDetailResponse.add(linkTo(methodOn(GameController.class).announceByExternalId(gameDetailResponse.getId(), null)).withRel("announce"));
        gameDetailResponse.add(linkTo(methodOn(GameController.class).fillByExternalId(gameDetailResponse.getId(), null)).withRel("fill"));
        gameDetailResponse.add(linkTo(methodOn(GameController.class).undoFillByExternalId(gameDetailResponse.getId())).withRel("undo"));
        gameDetailResponse.add(linkTo(methodOn(GameController.class).restartByExternalId(gameDetailResponse.getId())).withRel("restart"));
        gameDetailResponse.add(linkTo(methodOn(GameController.class).archiveByExternalId(gameDetailResponse.getId())).withRel("archive"));
        gameDetailResponse.add(linkTo(methodOn(GameController.class).completeByExternalId(gameDetailResponse.getId())).withRel("complete"));
        gameDetailResponse.setPlayer(modelMapper.map(playerService.getByExternalId(game.getPlayerId()), PlayerResponse.class));
        if (gameDetailResponse.getPlayer() != null) gameDetailResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(gameDetailResponse.getPlayer().getId())).withSelfRel());

        return gameDetailResponse;
    }

}
