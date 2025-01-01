package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.GameController;
import com.tejko.yamb.api.controllers.PlayerController;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.business.interfaces.PlayerService;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, GameResponse> {

    private final ModelMapper modelMapper;
    private final PlayerService playerService;

    @Autowired
    public GameModelAssembler(ModelMapper modelMapper, PlayerService playerService) {
        this.modelMapper = modelMapper;
        this.playerService = playerService;
    }

    @Override
    public GameResponse toModel(Game game) {
        
        GameResponse gameResponse = modelMapper.map(game, GameResponse.class);
        gameResponse.setPlayer(modelMapper.map(playerService.getByExternalId(game.getPlayerId()), PlayerResponse.class));
        if (gameResponse.getPlayer() != null) gameResponse.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(gameResponse.getPlayer().getId())).withSelfRel());

        return gameResponse;
    }

    public PagedModel<GameResponse> toPagedModel(Page<Game> gamePage) {
        Set<UUID> playerIds = gamePage.stream()
            .map(Game::getPlayerId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        List<Player> players = playerService.findAllByExternalIds(playerIds);

        Map<UUID, Player> playerMap = players.stream()
                .collect(Collectors.toMap(Player::getExternalId, Function.identity()));

        List<GameResponse> gameResponses = gamePage.stream().map(game -> {
            GameResponse response = modelMapper.map(game, GameResponse.class);

            UUID pid = game.getPlayerId();
            if (pid != null && playerMap.containsKey(pid)) {
                Player player = playerMap.get(pid);
                PlayerResponse playerResponse = modelMapper.map(player, PlayerResponse.class);
                response.setPlayer(playerResponse);
                if (response.getPlayer() != null) response.getPlayer().add(linkTo(methodOn(PlayerController.class).getByExternalId(response.getPlayer().getId())).withSelfRel());
                response.add(linkTo(methodOn(GameController.class).getByExternalId(response.getId())).withSelfRel());
            }

            return response;
        }).collect(Collectors.toList());

        PagedModel<GameResponse> pagedGames = PagedModel.of(
            gameResponses,
            new PagedModel.PageMetadata(
                gamePage.getSize(),
                gamePage.getNumber(),
                gamePage.getTotalElements(),
                gamePage.getTotalPages()
            )
        );

        return pagedGames;
    }
}
