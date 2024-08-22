package com.tejko.yamb.interfaces.services;

import java.util.UUID;

import com.tejko.yamb.api.payload.requests.ActionRequest;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.interfaces.BaseService;

public interface GameService extends BaseService<Game> {

    public Game play();

    public Game rollByExternalId(UUID externalId, ActionRequest actionRequest);

    public Game announceByExternalId(UUID externalId, ActionRequest actionRequest);

    public Game fillByExternalId(UUID externalId, ActionRequest actionRequest);

    public Game restartByExternalId(UUID externalId);
    
}
