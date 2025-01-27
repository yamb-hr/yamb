package com.tejko.yamb.api.controllers;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.hateoas.CollectionModel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.tejko.yamb.api.assemblers.PlayerModelAssembler;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.business.interfaces.WebSocketService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.util.ActivePlayerDirectory;

@RestController
public class WebSocketController {

    private final PlayerRepository playerRepo;
    private final PlayerModelAssembler playerModelAssembler;
    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketController(WebSocketService webSocketService, PlayerRepository playerRepo, 
                               PlayerModelAssembler playerModelAssembler) {
        this.webSocketService = webSocketService;
        this.playerRepo = playerRepo;
        this.playerModelAssembler = playerModelAssembler;
    }

    @MessageMapping("/public")
    public void publicMessage(String message, Principal principal) throws Exception {
        webSocketService.publicMessage(message, principal);
    }

    @MessageMapping("/private")
    public void privateMessage(String message, Principal principal) throws Exception {
        webSocketService.privateMessage(message, principal);
    }

    @MessageMapping("/clashes/{clashId}/reaction")
    public void handleReaction(@DestinationVariable UUID clashId, String message, Principal principal) throws Exception {
        webSocketService.handleReaction(clashId, message, principal);
    }

    @MessageMapping("/clashes/{clashId}/suggestion")
    public void handleSuggestion(@DestinationVariable UUID clashId, String message, Principal principal) throws Exception {
        webSocketService.handleSuggestion(clashId, message, principal);
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        webSocketService.handleSessionConnected(event);
        broadcastActivePlayers();   
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        webSocketService.handleSessionDisconnect(event);
        broadcastActivePlayers();
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        webSocketService.handleSessionSubscribeEvent(event);
    }
    
    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        webSocketService.handleSessionUnsubscribeEvent(event);
    }

    private void broadcastActivePlayers() {
        CollectionModel<PlayerResponse> players = playerModelAssembler.toCollectionModel(playerRepo.findAllByExternalIdIn(ActivePlayerDirectory.getActivePlayerExternalIdSet()));
        webSocketService.convertAndSend("/topic/players", players, MessageType.PLAYERS);
    }
}
