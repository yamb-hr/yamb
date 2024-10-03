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

import com.tejko.yamb.business.interfaces.GameService;
import com.tejko.yamb.domain.enums.BoxType;
import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.enums.ColumnType;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.security.AuthContext;
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
        Optional<Game> existingGame = gameRepo.findByPlayerIdAndStatusIn(playerExternalId, Arrays.asList(GameStatus.IN_PROGRESS, GameStatus.COMPLETED));
        Game game = existingGame.orElseGet(() -> gameRepo.save(Game.getInstance(playerExternalId)));
        return game;
    }

    @Override
    public Game rollByExternalId(UUID externalId, int[] diceToRoll) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.roll(diceToRoll);
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game announceByExternalId(UUID externalId, BoxType boxType) {
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.announce(boxType);
        gameRepo.save(game);
        advanceTurnIfWithinClash(game);
        return game;
    }

    @Override
    public Game fillByExternalId(UUID externalId, ColumnType columnType, BoxType boxType) {    
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.fill(columnType, boxType);
        if (game.getStatus() == GameStatus.COMPLETED) {
            Player player = AuthContext.getAuthenticatedPlayer();
            Score score = Score.getInstance(player, game.getTotalSum());
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        advanceTurnIfWithinClash(game);
        return game;
    }

    @Override
    public Game completeByExternalId(UUID externalId) {
        Game game = getByExternalId(externalId);
        game.complete();
        gameRepo.save(game);
        return game;
    }

    @Override
    public Game restartByExternalId(UUID externalId) {      
        Game game = getByExternalId(externalId);
        checkPermission(game.getPlayerId());
        game.restart();
        gameRepo.save(game);
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

    private void advanceTurnIfWithinClash(Game game) {
        Optional<Clash> existingClash = clashRepo.findByCurrentPlayerIdAndStatusAndType(game.getPlayerId(), ClashStatus.IN_PROGRESS, ClashType.LIVE);
        if (existingClash.isPresent()) {
            Clash clash = existingClash.get();
            clash.advanceTurn();
            if (clash.getOwnerId().equals(clash.getCurrentPlayerId()) && gameRepo.existsByPlayerIdAndStatus(clash.getCurrentPlayerId(), GameStatus.COMPLETED)) {
                clash.complete();
            }
            clashRepo.save(clash);
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
        gameRepo.deleteByExternalId(externalId);
    }


}
