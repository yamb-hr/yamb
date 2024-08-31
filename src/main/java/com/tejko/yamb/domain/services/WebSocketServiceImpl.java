package com.tejko.yamb.domain.services;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.enums.PlayerStatus;
import com.tejko.yamb.domain.models.WebSocketMessage;
import com.tejko.yamb.domain.services.interfaces.WebSocketService;
import com.tejko.yamb.websocket.PlayerSessionRegistry;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final PlayerSessionRegistry playerSessionRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketServiceImpl(PlayerSessionRegistry playerSessionRegistry, SimpMessagingTemplate simpMessagingTemplate) {
        this.playerSessionRegistry = playerSessionRegistry;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void publicMessage(WebSocketMessage message, Principal principal) {
        Long senderId = Long.valueOf(principal.getName());
        message.setSenderId(senderId);
        simpMessagingTemplate.convertAndSend("/chat/public", message);
    }

    @Override
    public void privateMessage(WebSocketMessage message, Principal principal) {
        Long senderId = Long.valueOf(principal.getName());
        message.setSenderId(senderId);
        Long receiverId = message.getReceiverId();
        simpMessagingTemplate.convertAndSendToUser(receiverId.toString(), "/private", message);
    }

    @Override
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            Long playerId = Long.valueOf(user.getName());
            playerSessionRegistry.updatePlayerStatus(playerId, PlayerStatus.ONLINE);
            System.out.println("Player ID " + playerId + " has connected...");
        } else {
            System.out.println("Session connected event received with no user information.");
        }
    }

    @Override
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            Long playerId = Long.valueOf(user.getName());
            playerSessionRegistry.updatePlayerStatus(playerId, PlayerStatus.OFFLINE);
            System.out.println("Player ID " + playerId + " has disconnected...");
            sendPublicChatMessage(playerId);
        } else {
            System.out.println("Session disconnect event received with no user information.");
        }
    }

    @Override
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            String destination = headers.getDestination();
            Long playerId = Long.valueOf(user.getName());
            System.out.println("Player ID " + playerId + " has subscribed to " + destination);
            if ("/chat/public".equals(destination)) {
                sendPublicChatMessage(playerId);
            }
        } else {
            System.out.println("Session subscribe event received with no user information.");
        }
    }

    
    private void sendPublicChatMessage(Long playerId) {
        WebSocketMessage message = new WebSocketMessage(playerId, null, MessageType.PLAYERS, playerSessionRegistry.getPlayerStatusMap());
        try {
            simpMessagingTemplate.convertAndSend("/chat/public", message);
        } catch (MessagingException e) {
            System.out.println("Error sending public chat message: " + e.getMessage());
        }
    }
}
