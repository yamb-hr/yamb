package com.tejko.yamb.business.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.GameService;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.entities.Game;
import com.tejko.yamb.domain.models.entities.Player;
import com.tejko.yamb.domain.models.entities.Score;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.repositories.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepo;
    private final ScoreRepository scoreRepo;

    @Autowired
    public GameServiceImpl(GameRepository gameRepo, ScoreRepository scoreRepo) {
        this.gameRepo = gameRepo;
        this.scoreRepo = scoreRepo;
    }

    @Override
    public Game getById(String id) {
        return gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public List<Game> getAll() {
        List<Game> games = gameRepo.findAllByOrderByUpdatedAtDesc();
        return games;
    }

    @Override
    public Game getOrCreate(Long playerId) {
        checkPermission(playerId);

        Optional<Game> existingGame = gameRepo.findByPlayerIdAndStatusIn(playerId, Arrays.asList(GameStatus.IN_PROGRESS, GameStatus.COMPLETED));
        Game game = existingGame.orElseGet(() -> gameRepo.save(Game.getInstance(playerId)));

        return game;
    }

    @Override
    public Game rollById(String id, int[] diceToRoll) {
        Game game = getById(id);
        checkPermission(game.getPlayerId());
        game.roll(diceToRoll);
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game announceById(String id, BoxType boxType) {
        Game game = getById(id);
        checkPermission(game.getPlayerId());
        game.announce(boxType);
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game fillById(String id, ColumnType columnType, BoxType boxType) {    
        Game game = getById(id);
        checkPermission(game.getPlayerId());
        game.fill(columnType, boxType);
        if (game.getStatus() == GameStatus.COMPLETED) {
            Player player = AuthContext.getAuthenticatedPlayer().get();
            Score score = Score.getInstance(player, game.getTotalSum());
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game completeById(String id) {
        Game game = getById(id);
        game.complete();
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game restartById(String id) {      
        Game game = getById(id);
        checkPermission(game.getPlayerId());
        game.restart();
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game archiveById(String id) {
        Game game = getById(id);
        checkPermission(game.getPlayerId());
        game.archive();
        gameRepo.save(game);
        return game;
    }

    private void checkPermission(Long playerId) {
        Optional<Player> authenticatedPlayerId = AuthContext.getAuthenticatedPlayer();  
        if (playerId == null || !authenticatedPlayerId.isPresent() || !authenticatedPlayerId.get().getId().equals(playerId)) {
            throw new AccessDeniedException("error.access_denied");
        }
    }


}
