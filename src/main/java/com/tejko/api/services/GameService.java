package com.tejko.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tejko.exceptions.IllegalMoveException;
import com.tejko.models.Score;
import com.tejko.models.Game;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.models.payload.GameResponse;
import com.tejko.repositories.ScoreRepository;
import com.tejko.utils.ModelMapper;
import com.tejko.repositories.PlayerRepository;
import com.tejko.repositories.GameRepository;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    ScoreRepository scoreRepository;

    ModelMapper mapper = new ModelMapper();

    public GameResponse getById(UUID id) {
        return mapper.mapToGameResponse(gameRepository.getById(id));
    }

    public Game getOtherById(UUID id) {
        return gameRepository.getById(id);
    }

    public GameResponse create() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        Game game = new Game(playerRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return mapper.mapToGameResponse(gameRepository.save(game));
    }

    public GameResponse rollDiceById(UUID id, List<Integer> diceToRoll) throws IllegalMoveException {
        Game game = gameRepository.getById(id);

        game.rollDice(diceToRoll);

        return mapper.mapToGameResponse(gameRepository.save(game));
    }

    public GameResponse announceById(UUID id, BoxType boxType) throws IllegalMoveException {
        Game game = gameRepository.getById(id);

        game.announce(boxType);

        return mapper.mapToGameResponse(gameRepository.save(game));
    }

    public GameResponse fillById(UUID id, ColumnType columnType, BoxType boxType) throws IllegalMoveException {
        Game game = gameRepository.getById(id);

        game.fillBox(columnType, boxType);

        if (game.getSheet().isCompleted()) {
            Score score = new Score(
                game.getPlayer(),
                game.getSheet().getTotalSum()
            );
            scoreRepository.save(score);
        }

        return  mapper.mapToGameResponse(gameRepository.save(game));
    }

    public GameResponse restartById(UUID id) {
        Game game = gameRepository.getById(id);
        game.restart();
        return mapper.mapToGameResponse(gameRepository.save(game));
    }

    public boolean hasPermission(UUID userId, UUID gameId) {
        return gameRepository.getById(gameId).getPlayer().getId().equals(userId);
    }

}
