package com.tejko.yamb.domain.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.util.I18nUtil;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.services.interfaces.GameService;
import com.tejko.yamb.domain.repositories.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepo;
    private final ScoreRepository scoreRepo;
    private final CustomObjectMapper mapper;
    private final I18nUtil i18nUtil;

    @Autowired
    public GameServiceImpl(GameRepository gameRepo, ScoreRepository scoreRepo, CustomObjectMapper mapper, I18nUtil i18nUtil) {
        this.gameRepo = gameRepo;
        this.scoreRepo = scoreRepo;
        this.mapper = mapper;
        this.i18nUtil = i18nUtil;
    }

    @Override
    public GameResponse getById(String id) {
        Game game = fetchById(id);
        return mapper.mapToResponse(game);
    }

    @Override
    public List<GameResponse> getAll() {
        List<Game> games = gameRepo.findAll();
        return mapper.mapCollection(games, mapper::mapToResponse, ArrayList::new);
    }

    @Override
    public GameResponse getOrCreate(GameRequest gameRequest) {
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer().get();
        checkPermission(gameRequest.getPlayerId());

        Optional<Game> existingGame = gameRepo.findByPlayerIdAndStatusIn(gameRequest.getPlayerId(), Arrays.asList(GameStatus.IN_PROGRESS, GameStatus.COMPLETED));
        Game game = existingGame.orElseGet(() -> gameRepo.save(Game.getInstance(gameRequest.getPlayerId(), authenticatedPlayer.getUsername())));

        GameResponse response = mapper.mapToResponse(game);
        response.setNew(!existingGame.isPresent());

        return response;
    }

    @Override
    public GameResponse rollById(String id, ActionRequest actionRequest) {
        Game game = fetchById(id);
        checkPermission(game.getPlayerId());
        game.roll(actionRequest.getDiceToRoll());
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse announceById(String id, ActionRequest actionRequest) {
        Game game = fetchById(id);
        checkPermission(game.getPlayerId());
        game.announce(actionRequest.getBoxType());
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse fillById(String id, ActionRequest actionRequest) {    
        Game game = fetchById(id);
        checkPermission(game.getPlayerId());
        game.fill(actionRequest.getColumnType(), actionRequest.getBoxType());
        if (game.getStatus() == GameStatus.COMPLETED) {
            Player player = AuthContext.getAuthenticatedPlayer().get();
            Score score = Score.getInstance(player, game.getTotalSum());
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse completeById(String id) {
        Game game = fetchById(id);
        game.complete();
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse restartById(String id) {      
        Game game = fetchById(id);
        checkPermission(game.getPlayerId());
        game.restart();
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public Game fetchById(String id) {
        return gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public GameResponse finishById(String id) {
        Game game = fetchById(id);
        checkPermission(game.getPlayerId());
        game.finish();
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    private void checkPermission(Long playerId) {
        Optional<Player> authenticatedPlayerId = AuthContext.getAuthenticatedPlayer();  
        if (playerId == null || !authenticatedPlayerId.isPresent() || !authenticatedPlayerId.get().getId().equals(playerId)) {
            throw new AccessDeniedException(i18nUtil.getMessage("error.access_denied"));
        }
    }


}
