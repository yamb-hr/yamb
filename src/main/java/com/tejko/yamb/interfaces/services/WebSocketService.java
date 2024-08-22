package com.tejko.yamb.interfaces.services;

import java.security.Principal;
import java.util.UUID;

import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.tejko.yamb.api.payload.WebSocketMessage;

public interface WebSocketService {

    public WebSocketMessage publicMessage(WebSocketMessage message, Principal principal);

    public WebSocketMessage privateMessage(WebSocketMessage message, Principal principal);

    public void handleSessionConnected(SessionConnectEvent event);

    public void handleSessionDisconnect(SessionDisconnectEvent event);

    public void handleSessionSubscribeEvent(SessionSubscribeEvent event);

    public void addPrincipal(String username, String principal);

    public String getPrincipalByExternalId(UUID externalId);
    
}
