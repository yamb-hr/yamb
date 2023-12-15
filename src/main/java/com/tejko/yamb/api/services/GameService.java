package com.tejko.yamb.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.models.Score;
import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.interfaces.RestService;
import com.tejko.yamb.models.Dice;
import com.tejko.yamb.models.Game;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.models.enums.BoxType;
import com.tejko.yamb.models.enums.GameStatus;
import com.tejko.yamb.models.payload.ActionRequest;
import com.tejko.yamb.repositories.ScoreRepository;
import com.tejko.yamb.util.Logger;
import com.tejko.yamb.repositories.PlayerRepository;
import com.tejko.yamb.repositories.GameRepository;

@Service
public class GameService implements RestService<Game> {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    ScoreRepository scoreRepo;
    
    @Autowired
    Logger logger;

    public Game getById(Long id) {
        return gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
    }

    public List<Game> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return gameRepo.findAll(pageable).getContent();
    }

    public Game play() { 
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Player player = playerRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        Optional<Game> game = gameRepo.findByPlayerIdAndStatus(player.getId(), GameStatus.IN_PROGRESS);
        if (game.isPresent() && game.get().getStatus() == GameStatus.IN_PROGRESS) {
            return game.get();
        } else {
            return gameRepo.save(Game.getInstance(player));
        }
    }

    public List<Dice> rollDiceById(Long id, ActionRequest actionRequest) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.rollDice(actionRequest.getDiceToRoll());
        gameRepo.save(game);
        logger.log("rollDice", actionRequest);
        return game.getDices();
    }

    public BoxType makeAnnouncementById(Long id, ActionRequest actionRequest) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.makeAnnouncement(actionRequest.getBoxType());
        gameRepo.save(game);
        logger.log("makeAnnouncement", actionRequest);
        return game.getAnnouncement();
    }

    public int fillBoxById(Long id, ActionRequest actionRequest) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.fillBox(actionRequest.getColumnType(), actionRequest.getBoxType());
        if (game.getStatus() == GameStatus.FINISHED) {
            Score score = Score.getInstance(
                game.getPlayer(),
                game.getSheet().getTotalSum()
            );
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        logger.log("fillBox", actionRequest);
        return game.getSheet().getColumns().get(actionRequest.getColumnType().ordinal()).getBoxes().get(actionRequest.getBoxType().ordinal()).getValue();
    }

    public Game restartById(Long id) {
        Game game = gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.restart();
        return gameRepo.save(game);
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
