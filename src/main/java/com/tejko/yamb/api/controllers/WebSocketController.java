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

import com.tejko.yamb.api.services.WebSocketService;
import com.tejko.yamb.models.payload.Message;

@RestController
public class WebSocketController {

    @Autowired
    WebSocketService webSocketService;
    

    @MessageMapping("/message")
    @SendTo("/chat/public")
    public Message publicMessage(Message message, Principal principal) throws Exception {
        return webSocketService.publicMessage(message, principal);
    }

    @MessageMapping("/private-message")
    @SendToUser("/private")
    public Message privateMessage(Message message, Principal principal) throws Exception {
        return webSocketService.privateMessage(message, principal);
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