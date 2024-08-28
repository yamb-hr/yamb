package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import com.tejko.yamb.api.dto.requests.ActionRequest;
import com.tejko.yamb.api.dto.requests.GameRequest;
import com.tejko.yamb.api.dto.responses.GameResponse;
import com.tejko.yamb.domain.models.Game;

public interface GameService {

    public Game fetchById(String id);

    public GameResponse getById(String id);

    public List<GameResponse> getAll();

    public GameResponse getOrCreate(GameRequest request);

    public GameResponse rollById(String id, ActionRequest actionRequest);

    public GameResponse announceById(String id, ActionRequest actionRequest);

    public GameResponse fillById(String id, ActionRequest actionRequest);

    public GameResponse restartById(String id);

    public GameResponse completeById(String id);

    public GameResponse finishById(String id);

}
