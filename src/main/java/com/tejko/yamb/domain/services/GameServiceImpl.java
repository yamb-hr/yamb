package com.tejko.yamb.domain.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.domain.services.interfaces.GameService;
import com.tejko.yamb.domain.repositories.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final AuthContext authContext;
    private final GameRepository gameRepo;
    private final ScoreRepository scoreRepo;
    private final CustomObjectMapper mapper;

    @Autowired
    public GameServiceImpl(AuthContext authContext, GameRepository gameRepo, ScoreRepository scoreRepo, CustomObjectMapper mapper) {
        this.authContext = authContext;
        this.gameRepo = gameRepo;
        this.scoreRepo = scoreRepo;
        this.mapper = mapper;
    }

    @Override
    public GameResponse getById(String id) {
        Game game = fetchById(id);
        return mapper.mapToResponse(game);
    }

    @Override
    public List<GameResponse> getAll() {
        List<Game> games = gameRepo.findAll();
        return games.stream().map(mapper::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public GameResponse create(GameRequest gameRequest) {
        Player authenticatedPlayer = authContext.getAuthenticatedPlayer().get();
        checkPermission(gameRequest.getPlayerId(), authenticatedPlayer.getId());

        Game game = gameRepo.findByPlayerIdAndStatusIn(gameRequest.getPlayerId(), Arrays.asList(GameStatus.IN_PROGRESS, GameStatus.COMPLETED))
            .orElseGet(() -> gameRepo.save(Game.getInstance(gameRequest.getPlayerId(), authenticatedPlayer.getUsername())));

        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse rollById(String id, ActionRequest actionRequest) {
        Player authenticatedPlayer = authContext.getAuthenticatedPlayer().get();
        Game game = fetchById(id);
        checkPermission(game.getPlayerId(), authenticatedPlayer.getId());
        game.roll(actionRequest.getDiceToRoll());
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse announceById(String id, ActionRequest actionRequest) {
        Player authenticatedPlayer = authContext.getAuthenticatedPlayer().get();
        Game game = fetchById(id);
        checkPermission(game.getPlayerId(), authenticatedPlayer.getId());
        game.announce(actionRequest.getBoxType());
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse fillById(String id, ActionRequest actionRequest) {    
        Player authenticatedPlayer = authContext.getAuthenticatedPlayer().get();    
        Game game = fetchById(id);
        checkPermission(game.getPlayerId(), authenticatedPlayer.getId());
        game.fill(actionRequest.getColumnType(), actionRequest.getBoxType());
        if (game.getStatus() == GameStatus.COMPLETED) {
            Player player = authContext.getAuthenticatedPlayer().get();
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
        Player authenticatedPlayer = authContext.getAuthenticatedPlayer().get();  
        Game game = fetchById(id);
        checkPermission(game.getPlayerId(), authenticatedPlayer.getId());
        game.restart();
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public Game fetchById(String id) {
        return gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
    }

    @Override
    public GameResponse finishById(String id) {
        Player authenticatedPlayer = authContext.getAuthenticatedPlayer().get();
        Game game = fetchById(id);
        checkPermission(game.getPlayerId(), authenticatedPlayer.getId());
        game.finish();
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    
    private void checkPermission(Long playerId, Long authenticatedPlayerId) {
        if (!playerId.equals(authenticatedPlayerId)) {
            throw new IllegalArgumentException(MessageConstants.ERROR_FORBIDDEN);
        }
    }

}
