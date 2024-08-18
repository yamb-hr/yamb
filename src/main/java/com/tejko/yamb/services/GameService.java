package com.tejko.yamb.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.api.payload.requests.ActionRequest;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.interfaces.BaseService;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.util.Logger;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.GameRepository;

@Service
public class GameService implements BaseService<Game> {

    @Autowired
    GameRepository gameRepo;

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    ScoreRepository scoreRepo;

    @Autowired
    Logger logger;

    public Game getByExternalId(UUID externalId) {
        return gameRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
    }

    public List<Game> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return gameRepo.findAll(pageable).getContent();
    }

    public Game play() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Player player = playerRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        Optional<Game> game = gameRepo.findByPlayerIdAndStatus(player.getId(), GameStatus.IN_PROGRESS);
        if (game.isPresent() && game.get().getStatus() == GameStatus.IN_PROGRESS) {
            return game.get();
        } else {
            return gameRepo.save(Game.getInstance(player));
        }
    }

    public Game rollDiceByExternalId(UUID externalId, ActionRequest actionRequest) {
        Game game = gameRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.rollDice(actionRequest.getDiceToRoll());
        gameRepo.save(game);
        logger.log("rollDice", actionRequest);
        return game;
    }

    public Game makeAnnouncementByExternalId(UUID externalId, ActionRequest actionRequest) {
        Game game = gameRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.makeAnnouncement(actionRequest.getBoxType());
        gameRepo.save(game);
        logger.log("makeAnnouncement", actionRequest);
        return game;
    }

    public Game fillBoxByExternalId(UUID externalId, ActionRequest actionRequest) {
        Game game = gameRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.fillBox(actionRequest.getColumnType(), actionRequest.getBoxType());
        if (game.getStatus() == GameStatus.FINISHED) {
            Score score = Score.getInstance(
                    game.getPlayer(),
                    game.getTotalSum());
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        logger.log("fillBox", actionRequest);
        return game;
    }

    public Game restartByExternalId(UUID externalId) {
        Game game = gameRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
        game.restart();
        return gameRepo.save(game);
    }

    public void deleteByExternalId(UUID externalId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByExternalId'");
    }

}
