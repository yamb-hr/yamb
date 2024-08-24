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

import com.tejko.yamb.api.payload.WebSocketMessage;
import com.tejko.yamb.interfaces.services.WebSocketService;

@RestController
public class WebSocketController {

    private final WebSocketService webSocketService;

    @Autowired
    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @MessageMapping("/message")
    @SendTo("/chat/public")
    public void publicMessage(WebSocketMessage message, Principal principal) throws Exception {
        webSocketService.publicMessage(message, principal);
    }

    @MessageMapping("/private-message")
    @SendToUser("/private")
    public void privateMessage(WebSocketMessage message, Principal principal) throws Exception {
        webSocketService.privateMessage(message, principal);
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

}