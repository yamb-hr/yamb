package com.tejko.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tejko.models.Score;
import com.tejko.constants.GameConstants;
import com.tejko.models.Game;
import com.tejko.models.Player;
import com.tejko.models.enums.BoxType;
import com.tejko.models.enums.ColumnType;
import com.tejko.repositories.ScoreRepository;
import com.tejko.security.SessionManager;
import com.tejko.repositories.PlayerRepository;
import com.tejko.repositories.GameRepository;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    ScoreRepository scoreRepo;

    @Autowired
    SessionManager sessionManager;

    public Game getById(Long id) {
        return gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_GAME_NOT_FOUND));
    }

    public List<Game> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return gameRepo.findAll(pageable).getContent();
    }

    public Game create() { 
        Player player;
        if (isAnonymousUser()) {
            if (sessionManager.getPlayerFromSession() == null) {
                player = sessionManager.createPlayerSession();
            } else {
                player = sessionManager.getPlayerFromSession();
            }
        } else {
            player = playerRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new ResourceNotFoundException("Not Found by Username"));
        }
        Game game = Game.getInstance(player);
        return gameRepo.save(game);
    }

    private boolean isAnonymousUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
    }

    public Game rollDiceById(Long id, List<Integer> diceToRoll) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_GAME_NOT_FOUND));
        game.rollDice(diceToRoll);
        return gameRepo.save(game);
    }

    public Game announceById(Long id, BoxType boxType) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_GAME_NOT_FOUND));
        game.announce(boxType);
        return gameRepo.save(game);
    }

    public Game fillById(Long id, ColumnType columnType, BoxType boxType) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_GAME_NOT_FOUND));
        game.fillBox(columnType, boxType);
        if (game.getSheet().isCompleted()) {
            Score score = Score.getInstance(
                game.getPlayer(),
                game.getSheet().getTotalSum()
            );
            scoreRepo.save(score);
        }
        return  gameRepo.save(game);
    }

    public Game restartById(Long id) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_GAME_NOT_FOUND));
        game.restart();
        return gameRepo.save(game);
    }

}
