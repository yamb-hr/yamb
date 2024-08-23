package com.tejko.yamb.services;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.interfaces.services.WebSocketService;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<UUID, PlayerStatus> playerStatusMap = new HashMap<>();

    private Map<UUID, String> externalIdPrincipalMap = new HashMap<>();

    public void publicMessage(WebSocketMessage message, Principal principal) {
        Optional.ofNullable(principal)
            .map(Principal::getName)
            .flatMap(this::getExternalIdFromPrincipal)
            .ifPresent(externalId -> {
                String username = getUsernameByExternalId(externalId);
                message.setSender(username);
                System.out.println("Message sent to all from " + message.getSender() + ": " + message.getContent());
            });
    }

    public void privateMessage(WebSocketMessage message, Principal principal) {
        Optional.ofNullable(principal)
            .map(Principal::getName)
            .flatMap(this::getExternalIdFromPrincipal)
            .ifPresent(senderExternalId -> {
                String senderUsername = getUsernameByExternalId(senderExternalId);
                message.setSender(senderUsername);
                playerRepo.findByUsername(message.getReceiver())
                    .map(Player::getExternalId)
                    .flatMap(receiverExternalId -> Optional.ofNullable(externalIdPrincipalMap.get(receiverExternalId)))
                    .ifPresent(receiverPrincipal -> {
                        simpMessagingTemplate.convertAndSendToUser(receiverPrincipal, "/private", message);
                        System.out.println("Message sent to " + message.getReceiver() + " from " + message.getSender() + ": " + message.getContent());
                    });
            });
    }

    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Optional.ofNullable(headers.getUser())
            .map(Principal::getName)
            .flatMap(this::getExternalIdFromPrincipal)
            .ifPresent(externalId -> {
                playerStatusMap.put(externalId, PlayerStatus.ONLINE);
                String username = getUsernameByExternalId(externalId);
                System.out.println(username + " has connected... ");
            });
    }

    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Optional.ofNullable(headers.getUser())
            .map(Principal::getName)
            .flatMap(this::getExternalIdFromPrincipal)
            .ifPresent(externalId -> {
                if (playerStatusMap.containsKey(externalId)) {
                    playerStatusMap.remove(externalId);
                    String username = getUsernameByExternalId(externalId);
                    System.out.println(username + " has disconnected... ");
                }
                try {
                    WebSocketMessage message = new WebSocketMessage("Server", "all", MessageType.PLAYERS, playerStatusMap);
                    simpMessagingTemplate.convertAndSend("/chat/public", message);
                } catch (Exception e) {
                    playerStatusMap.clear();
                    System.out.println(e.getMessage());
                }
            });
    }

    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();
        Optional.ofNullable(headers.getUser())
            .map(Principal::getName)
            .flatMap(this::getExternalIdFromPrincipal)
            .ifPresent(externalId -> {
                String username = getUsernameByExternalId(externalId);
                System.out.println(username + " has subscribed to " + destination);
                if ("/chat/public".equals(destination)) {
                    sendPublicChatMessage(username);
                }
            });
    }

    private void sendPublicChatMessage(String username) {
        try {
            WebSocketMessage message = new WebSocketMessage("Server", username, MessageType.PLAYERS, playerStatusMap);
            simpMessagingTemplate.convertAndSend("/chat/public", message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPrincipal(UUID externalId, String principal) {
        externalIdPrincipalMap.put(externalId, principal);
    }

    private Optional<UUID> getExternalIdFromPrincipal(String principal) {
        return externalIdPrincipalMap.entrySet().stream()
            .filter(entry -> entry.getValue().equals(principal))
            .map(Map.Entry::getKey)
            .findFirst();
    }

    public String getPrincipalByExternalId(UUID externalId) {
        return externalIdPrincipalMap.get(externalId);
    }

    private String getUsernameByExternalId(UUID externalId) {
        return playerRepo.findByExternalId(externalId)
            .map(Player::getUsername)
            .orElse("Unknown");
    }
}
