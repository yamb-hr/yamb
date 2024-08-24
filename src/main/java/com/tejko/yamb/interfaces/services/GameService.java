package com.tejko.yamb.interfaces.services;

import java.util.List;

import com.tejko.yamb.api.payload.requests.ActionRequest;
import com.tejko.yamb.api.payload.requests.GameRequest;
import com.tejko.yamb.api.payload.responses.GameResponse;
import com.tejko.yamb.domain.models.Game;

public interface GameService {

    public Game fetchById(String id);

    public GameResponse getById(String id);

    public List<GameResponse> getAll();

    public GameResponse create(GameRequest request);

    public GameResponse rollById(String id, ActionRequest actionRequest);

    public GameResponse announceById(String id, ActionRequest actionRequest);

    public GameResponse fillById(String id, ActionRequest actionRequest);

    public GameResponse restartById(String id);
    
    public void deleteById(String id);

}
