package com.tejko.yamb.api.events.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.assemblers.GameDetailModelAssembler;
import com.tejko.yamb.api.dto.responses.GameDetailResponse;
import com.tejko.yamb.api.events.GameUpdatedEvent;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.util.WebSocketManager;

@Component
public class GameUpdatedEventHandler {

    private final WebSocketManager webSocketManager;
    private final GameDetailModelAssembler gameDetailModelAssembler;

    @Autowired
    public GameUpdatedEventHandler(WebSocketManager webSocketManager, GameDetailModelAssembler gameDetailModelAssembler) {
        this.webSocketManager = webSocketManager;
        this.gameDetailModelAssembler = gameDetailModelAssembler;
    }

    @EventListener
    public void handleGameUpdated(GameUpdatedEvent event) {
        GameDetailResponse gameDetailResponse = gameDetailModelAssembler.toModel(event.getGame());
        webSocketManager.convertAndSend(
            "/topic/games/" + gameDetailResponse.getId(),
            gameDetailResponse, 
            MessageType.GAME
        );
    }
    
}
