package com.tejko.yamb.services;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.interfaces.services.WebSocketService;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<String, PlayerStatus> playerStatusMap = new HashMap<>();

    private Map<String, String> usernamePrincipalMap = new HashMap<>();

    public WebSocketMessage publicMessage(WebSocketMessage message, Principal principal) {
        message.setSender(getUsernameFromPrincipal(principal.getName()));
        System.out.println("Message sent to all from " + message.getSender() + ": " + message.getContent());
        return message;
    }

    public WebSocketMessage privateMessage(WebSocketMessage message, Principal principal) {
        message.setSender(getUsernameFromPrincipal(principal.getName()));
        simpMessagingTemplate.convertAndSendToUser(usernamePrincipalMap.get(message.getReceiver()).toString(),
                "/private", message);
        System.out.println("Message sent to " + message.getReceiver() + " from " + message.getSender() + ": "
                + message.getContent());
        return null;
    }

    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headers.getUser();
        if (principal != null) {
            String username = getUsernameFromPrincipal(principal.getName());
            playerStatusMap.put(username, PlayerStatus.ONLINE);
            System.out.println(username + " has connected... ");
        }
    }

    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headers.getUser();
        if (principal != null) {
            try {
                String username = getUsernameFromPrincipal(principal.getName());
                if (playerStatusMap.containsKey(username)) {
                    playerStatusMap.remove(username);
                    System.out.println(username + " has disconnected... ");
                }
                WebSocketMessage message = new WebSocketMessage("Server", "all", MessageType.PLAYERS, playerStatusMap);
                simpMessagingTemplate.convertAndSend("/chat/public", message);
            } catch (Exception e) {
                playerStatusMap.clear();
                System.out.println(e.getMessage());
            }            
        }
    }

    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();
        Principal principal = headers.getUser();
        if (principal != null) {
            String username = getUsernameFromPrincipal(principal.getName());
            System.out.println(username + " has subscribed to " + destination);
            if ("/chat/public".equals(destination)) {
                try {
                    WebSocketMessage message = new WebSocketMessage("Server", username, MessageType.PLAYERS, playerStatusMap);
                    simpMessagingTemplate.convertAndSend("/chat/public", message);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void addPrincipal(String username, String principal) {
        usernamePrincipalMap.put(username, principal);
    }

    private String getUsernameFromPrincipal(String principal) {
        for (Map.Entry<String, String> entry : usernamePrincipalMap.entrySet()) {
            if (entry.getValue().equals(principal)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public String getPrincipalByExternalId(UUID externalId) {
        return usernamePrincipalMap.get(playerRepo.findByExternalId(externalId).get().getUsername());
    }

}
