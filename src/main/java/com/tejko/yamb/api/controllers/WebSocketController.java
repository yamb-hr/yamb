package com.tejko.yamb.api.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.tejko.yamb.business.interfaces.WebSocketService;
import com.tejko.yamb.domain.models.WebSocketMessage;

@RestController
public class WebSocketController {

    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/public")
    @SendTo("/topic/public")
    public void publicMessage(WebSocketMessage messageRequest, Principal principal) throws Exception {
        webSocketService.publicMessage(messageRequest, principal);
    }

    @MessageMapping("/private")
    @SendToUser("/player/private")
    public void privateMessage(WebSocketMessage messageRequest, Principal principal) throws Exception {
        webSocketService.privateMessage(messageRequest, principal);
    }

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        webSocketService.handleSessionConnected(event);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        webSocketService.handleSessionDisconnect(event);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        webSocketService.handleSessionSubscribeEvent(event);
    }
    
    @EventListener
    public void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event) {
        webSocketService.handleSessionUnsubscribeEvent(event);
    }
}
