package com.tejko.yamb.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.tejko.yamb.api.payload.WebSocketMessage;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.enums.PlayerStatus;
import com.tejko.yamb.interfaces.services.WebSocketService;
import com.tejko.yamb.security.PlayerSessionRegistry;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PlayerSessionRegistry playerSessionRegistry;

    @Autowired
    public WebSocketServiceImpl(SimpMessagingTemplate simpMessagingTemplate, PlayerSessionRegistry playerSessionRegistry) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.playerSessionRegistry = playerSessionRegistry;
    }

    @Override
    public void publicMessage(WebSocketMessage message, Principal principal) {
        Long senderId = Long.valueOf(principal.getName());
        message.setSenderId(senderId);
        System.out.println("Message sent to all from Player ID " + senderId + ": " + message.getContent());
        simpMessagingTemplate.convertAndSend("/chat/public", message);
    }

    @Override
    public void privateMessage(WebSocketMessage message, Principal principal) {
        Long senderId = Long.valueOf(principal.getName());
        message.setSenderId(senderId);

        Long receiverId = message.getReceiverId();
        simpMessagingTemplate.convertAndSendToUser(receiverId.toString(), "/private", message);
        System.out.println("Message sent to Player ID " + receiverId + " from Player ID " + senderId + ": " + message.getContent());
    }

    @Override
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Long playerId = Long.valueOf(headers.getUser().getName());

        playerSessionRegistry.updatePlayerStatus(playerId, PlayerStatus.ONLINE);
        System.out.println("Player ID " + playerId + " has connected...");
    }

    @Override
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Long playerId = Long.valueOf(headers.getUser().getName());

        playerSessionRegistry.updatePlayerStatus(playerId, PlayerStatus.OFFLINE);
        System.out.println("Player ID " + playerId + " has disconnected...");
        try {
            WebSocketMessage message = new WebSocketMessage(null, null, MessageType.PLAYERS, playerSessionRegistry.getPlayerStatusMap());
            simpMessagingTemplate.convertAndSend("/chat/public", message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();
        Long playerId = Long.valueOf(headers.getUser().getName());

        System.out.println("Player ID " + playerId + " has subscribed to " + destination);
        if ("/chat/public".equals(destination)) {
            sendPublicChatMessage(playerId);
        }
    }

    private void sendPublicChatMessage(Long playerId) {
        try {
            WebSocketMessage message = new WebSocketMessage(null, playerId, MessageType.PLAYERS, playerSessionRegistry.getPlayerStatusMap());
            simpMessagingTemplate.convertAndSend("/chat/public", message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
