package com.tejko.yamb.business.interfaces;

import java.security.Principal;

import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.tejko.yamb.domain.enums.MessageType;
import com.tejko.yamb.domain.models.WebSocketMessage;

public interface WebSocketService {

    void publicMessage(WebSocketMessage messageRequest, Principal principal);

    void privateMessage(WebSocketMessage messageRequest, Principal principal);

    void handleSessionConnected(SessionConnectEvent event);

    void handleSessionDisconnect(SessionDisconnectEvent event);

    void handleSessionSubscribeEvent(SessionSubscribeEvent event);
    
    void handleSessionUnsubscribeEvent(SessionUnsubscribeEvent event);
    
    void convertAndSend(String destination, Object content, MessageType type);
}
