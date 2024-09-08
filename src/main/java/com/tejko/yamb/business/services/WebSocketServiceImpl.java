package com.tejko.yamb.business.services;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.tejko.yamb.business.interfaces.WebSocketService;
import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.enums.PlayerStatus;
import com.tejko.yamb.domain.models.WebSocketMessage;
import com.tejko.yamb.websocket.PlayerSessionRegistry;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServiceImpl.class);

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
            logger.info("Player ID " + playerId + " has connected...");
        } else {
            logger.info("Session connected event received with no user information.");
        }
    }

    @Override
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            Long playerId = Long.valueOf(user.getName());
            playerSessionRegistry.updatePlayerStatus(playerId, PlayerStatus.OFFLINE);
            logger.info("Player ID " + playerId + " has disconnected...");
            sendPublicChatMessage(playerId);
        } else {
            logger.info("Session disconnect event received with no user information.");
        }
    }

    @Override
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headers.getUser();
        if (user != null) {
            String destination = headers.getDestination();
            Long playerId = Long.valueOf(user.getName());
            logger.info("Player ID " + playerId + " has subscribed to " + destination);
            if ("/chat/public".equals(destination)) {
                sendPublicChatMessage(playerId);
            }
        } else {
            logger.info("Session subscribe event received with no user information.");
        }
    }

    
    private void sendPublicChatMessage(Long playerId) {
        WebSocketMessage message = new WebSocketMessage(playerId, null, MessageType.PLAYERS, playerSessionRegistry.getPlayerStatusMap());
        try {
            simpMessagingTemplate.convertAndSend("/chat/public", message);
        } catch (MessagingException e) {
            logger.info("Error sending public chat message: " + e.getMessage());
        }
    }
}
