package com.tejko.yamb.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Score;
import com.tejko.yamb.api.payload.requests.ActionRequest;
import com.tejko.yamb.api.payload.requests.GameRequest;
import com.tejko.yamb.api.payload.responses.GameResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.interfaces.services.GameService;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.ObjectMapper;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.util.YambLogger;
import com.tejko.yamb.domain.repositories.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final AuthContext authContext;
    private final GameRepository gameRepo;
    private final ScoreRepository scoreRepo;
    private final YambLogger logger;
    private final ObjectMapper mapper;

    @Autowired
    public GameServiceImpl(AuthContext authContext, GameRepository gameRepo, ScoreRepository scoreRepo, YambLogger logger, ObjectMapper mapper) {
        this.authContext = authContext;
        this.gameRepo = gameRepo;
        this.scoreRepo = scoreRepo;
        this.logger = logger;
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
        // check if a player already has a game which is not completed
        //validatePlayer(gameRequest.getPlayerId());
        // Game game = gameRepo.findById(player.getActiveGameId()).orElse(gameRepo.save(Game.getInstance(player.getId(), player.getUsername())));
        // return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse rollById(String id, ActionRequest actionRequest) {
        Game game = fetchById(id);
        game.roll(actionRequest.getDiceToRoll());
        gameRepo.save(game);
        logger.log("roll", actionRequest);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse announceById(String id, ActionRequest actionRequest) {
        Game game = fetchById(id);
        game.announce(actionRequest.getBoxType());
        gameRepo.save(game);
        logger.log("announce", actionRequest);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse fillById(String id, ActionRequest actionRequest) {        
        Game game = fetchById(id);
        game.fill(actionRequest.getColumnType(), actionRequest.getBoxType());
        if (game.getStatus() == GameStatus.FINISHED) {
            Player player = authContext.getAuthenticatedPlayer().get();
            Score score = Score.getInstance(player, game.getTotalSum());
            scoreRepo.save(score);
        }
        gameRepo.save(game);
        logger.log("fill", actionRequest);
        return mapper.mapToResponse(game);
    }

    @Override
    public GameResponse restartById(String id) {        
        Game game = fetchById(id);
        game.restart();
        gameRepo.save(game);
        return mapper.mapToResponse(game);
    }

    @Override
    public void deleteById(String id) {
        gameRepo.deleteById(id);
    }

    @Override
    public Game fetchById(String id) {
        return gameRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_GAME_NOT_FOUND));
    }

}
