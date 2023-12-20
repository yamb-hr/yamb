package com.tejko.yamb.api.services;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.tejko.yamb.models.enums.MessageType;
import com.tejko.yamb.models.enums.PlayerStatus;
import com.tejko.yamb.models.payload.Message;
import com.tejko.yamb.models.payload.PrincipalResponse;
import com.tejko.yamb.security.JwtUtil;

@Service
public class WebSocketService {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PlayerService playerService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private Map<String, PlayerStatus> playerStatusMap = new HashMap<>();
    
    private Map<String, String> usernamePrincipalMap = new HashMap<>();

    public Message publicMessage(Message message, Principal principal) throws Exception {
        message.setSender(getUsernameFromPrincipal(principal.getName()));
        System.out.println("Message sent to all from " + message.getSender() + ": " + message.getContent());
        return message;
    }

    public Message privateMessage(Message message, Principal principal) throws Exception {
        message.setSender(getUsernameFromPrincipal(principal.getName()));
        simpMessagingTemplate.convertAndSendToUser(usernamePrincipalMap.get(message.getReceiver()).toString(), "/private", message);
        System.out.println("Message sent to " + message.getReceiver() + " from " + message.getSender() + ": " + message.getContent());
        return null;
    }

    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headers.getUser();
        String username = getUsernameFromPrincipal(principal.getName());
        System.out.println(playerStatusMap);
        playerStatusMap.put(username, PlayerStatus.ONLINE);
        System.out.println(playerStatusMap);
        System.out.println(username + " has connected... ");
    }

    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = headers.getUser();
        String username = getUsernameFromPrincipal(principal.getName());
        playerStatusMap.put(username, PlayerStatus.OFFLINE);
        System.out.println(username + " has disconnected... ");
        Message message = new Message("Server", "all", MessageType.PLAYERS, playerStatusMap);
        simpMessagingTemplate.convertAndSend("/chat/public", message);
    }

    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headers.getDestination();
        Principal principal = headers.getUser();
        String username = getUsernameFromPrincipal(principal.getName());
        System.out.println(username + " has subscribed to " + destination);
        if ("/chat/public".equals(destination) && principal != null) {
            Message message = new Message("Server", username, MessageType.PLAYERS, playerStatusMap);
            simpMessagingTemplate.convertAndSend("/chat/public", message);
        }
    }

    public void addPrincipal(String username, String principal) {
        usernamePrincipalMap.put(username, principal);
        System.out.println("Added principal " + principal + " to username " + username);
    }

    private String getUsernameFromPrincipal(String principal) {
        for (Map.Entry<String, String> entry : usernamePrincipalMap.entrySet()) {
            if (entry.getValue().equals(principal)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public PrincipalResponse getPrincipalByPlayerId(Long playerId) {
        return new PrincipalResponse(usernamePrincipalMap.get(playerService.getById(playerId).getUsername()));
    }
    
}
