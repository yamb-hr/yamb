package com.tejko.yamb.business.services;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.events.ClashUpdatedEvent;
import com.tejko.yamb.api.events.GameUpdatedEvent;
import com.tejko.yamb.business.interfaces.GameService;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.enums.GameType;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.ApplicationContextProvider;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.repositories.ClashRepository;
import com.tejko.yamb.domain.repositories.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepo;
    private final ScoreRepository scoreRepo;
    private final ClashRepository clashRepo;

    @Autowired
    public GameServiceImpl(GameRepository gameRepo, ScoreRepository scoreRepo, ClashRepository clashRepo) {
        this.gameRepo = gameRepo;
        this.scoreRepo = scoreRepo;
        this.clashRepo = clashRepo;
    }

    @Override
    public Game getByExternalId(UUID externalId) {
        return gameRepo.findByExternalId(externalId).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public Page<Game> getAll(Pageable pageable) {
        return gameRepo.findAll(pageable);
    }

    @Override
    public Game getOrCreate(UUID playerExternalId) {
        Optional<Game> existingGame = gameRepo.findByPlayerIdAndTypeAndStatusIn(playerExternalId, GameType.NORMAL, Arrays.asList(GameStatus.IN_PROGRESS, GameStatus.COMPLETED));
        Game game = existingGame.orElseGet(() -> gameRepo.save(Game.getInstance(playerExternalId, GameType.NORMAL)));
        return game;
    }

    @Override
    public Game rollByExternalId(UUID externalId, int[] diceToRoll) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        if (GameType.CLASH.equals(game.getType())) {
            validateTurnByGameExternalId(externalId);
        }
        game.roll(diceToRoll);
        gameRepo.save(game);
        ApplicationContextProvider.publishEvent(new GameUpdatedEvent(game));
        return game;
    }

    @Override
    public Game announceByExternalId(UUID externalId, BoxType boxType) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        if (GameType.CLASH.equals(game.getType())) {
            validateTurnByGameExternalId(externalId);
        }
        game.announce(boxType);
        gameRepo.save(game);
        ApplicationContextProvider.publishEvent(new GameUpdatedEvent(game));
        return game;
    }

    @Override
    public Game fillByExternalId(UUID externalId, ColumnType columnType, BoxType boxType) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        if (GameType.CLASH.equals(game.getType())) {
            validateTurnByGameExternalId(externalId);
        }
        game.fill(columnType, boxType);
        if (game.getStatus() == GameStatus.COMPLETED) {
            Player player = AuthContext.getAuthenticatedPlayer();
            Score score = Score.getInstance(player, game.getTotalSum());
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        if (GameType.CLASH.equals(game.getType())) {
            advanceTurnByGameExternalId(externalId);
        }
        ApplicationContextProvider.publishEvent(new GameUpdatedEvent(game));
        return game;
    }

    public Game undoFillByExternalId(UUID externalId) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.undoFill();
        gameRepo.save(game);
        ApplicationContextProvider.publishEvent(new GameUpdatedEvent(game));
        return game;
    }


    @Override
    public Game completeByExternalId(UUID externalId) {
        Game game = getByExternalId(externalId);
        game.complete();
        gameRepo.save(game);
        ApplicationContextProvider.publishEvent(new GameUpdatedEvent(game));
        return game;
    }

    @Override
    public Game restartByExternalId(UUID externalId) {      
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.restart();
        gameRepo.save(game);
        ApplicationContextProvider.publishEvent(new GameUpdatedEvent(game));
        return game;
    }

    @Override
    public Game archiveByExternalId(UUID externalId) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.archive();
        gameRepo.save(game);
        return game;
    }
    
    private void validateTurnByGameExternalId(UUID gameExternalId) {
        Game game = getByExternalId(gameExternalId);
        Clash clash = clashRepo.findByGameId(gameExternalId).get();
        UUID currentPlayerId = clash.getPlayers().get(clash.getTurn()).getId();
        if (!game.getPlayerId().equals(currentPlayerId)) {
            throw new IllegalStateException("Not your turn");
        }
    }

    private void advanceTurnByGameExternalId(UUID gameExternalId) {
        Optional<Clash> existingClash = clashRepo.findByGameId(gameExternalId);
        if (existingClash.isPresent()) {
            Clash clash = existingClash.get();
            clash.advanceTurn();
            if (clash.getOwnerId().equals(clash.getPlayers().get(clash.getTurn()).getId()) && gameRepo.existsByPlayerIdAndStatus(clash.getOwnerId(), GameStatus.COMPLETED)) {
                clash.complete();
            }
            clashRepo.save(clash);
            ApplicationContextProvider.publishEvent(new ClashUpdatedEvent(clash));
        }
    }

    private void checkPermission(UUID playerExternalId) {
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer();  
        if (playerExternalId == null || authenticatedPlayer != null && !authenticatedPlayer.getExternalId().equals(playerExternalId)) {
            throw new AccessDeniedException("error.access_denied");
        }
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        Game game = getByExternalId(externalId);
        gameRepo.delete(game);
    }

    @Override
    public void deleteAll() {
        gameRepo.deleteAll();
    }

}
