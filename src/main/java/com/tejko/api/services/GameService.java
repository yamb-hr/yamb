package com.tejko.api.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tejko.exceptions.IllegalMoveException;
import com.tejko.models.Score;
import com.tejko.models.Game;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.repositories.ScoreRepository;
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

    public Game getById(UUID id) {
        return gameRepository.getById(id);
    }

    public List<Game> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return gameRepository.findAll(pageable).getContent();
    }

    public Game create() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        Game game = new Game(playerRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return gameRepository.save(game);
    }

    public Game rollDiceById(UUID id, List<Integer> diceToRoll) throws IllegalMoveException {
        Game game = gameRepository.getById(id);

        game.rollDice(diceToRoll);

        return gameRepository.save(game);
    }

    public Game announceById(UUID id, BoxType boxType) throws IllegalMoveException {
        Game game = gameRepository.getById(id);

        game.announce(boxType);

        return gameRepository.save(game);
    }

    public Game fillById(UUID id, ColumnType columnType, BoxType boxType) throws IllegalMoveException {
        Game game = gameRepository.getById(id);

        game.fillBox(columnType, boxType);

        if (game.getSheet().isCompleted()) {
            Score score = new Score(
                game.getPlayer(),
                game.getSheet().getTotalSum()
            );
            scoreRepository.save(score);
        }

        return  gameRepository.save(game);
    }

    public Game restartById(UUID id) {
        Game game = gameRepository.getById(id);
        game.restart();
        return gameRepository.save(game);
    }

    public boolean hasPermission(UUID userId, UUID gameId) {
        return gameRepository.getById(gameId).getPlayer().getId().equals(userId);
    }

}
