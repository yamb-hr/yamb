package com.tejko.yamb.business.interfaces;

import java.security.Principal;
import java.util.UUID;

import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.tejko.yamb.domain.enums.MessageType;

public interface WebSocketService {

    void publicMessage(String message, Principal principal);

    void privateMessage(String message, Principal principal);

    void handleReaction(UUID clashId, String message, Principal principal);
    
    void handleSuggestion(UUID clashId, String message, Principal principal);

    void handleSessionConnected(SessionConnectEvent event);

    void handleSessionDisconnect(SessionDisconnectEvent event);

    void handleSessionSubscribeEvent(SessionSubscribeEvent event);
    
    void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event);
    
    void convertAndSend(String destination, Object content, MessageType type);
}
