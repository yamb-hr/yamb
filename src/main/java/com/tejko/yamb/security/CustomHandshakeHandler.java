package com.tejko.yamb.security;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.tejko.yamb.interfaces.services.WebSocketService;

@Component
@Scope("prototype")
public class CustomHandshakeHandler extends DefaultHandshakeHandler {
	
    @Autowired
    JwtUtil jwtUtil;
    
    @Autowired
    WebSocketService webSocketService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            String token = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams().getFirst("token");
            if (token != null && jwtUtil.validateToken(token)) {
                String principal = UUID.randomUUID().toString();
                String username = jwtUtil.extractUsernameFromToken(token);
                if (username != null) {
                    webSocketService.addPrincipal(username, principal);
                    return new StompPrincipal(principal.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
        throw new HandshakeFailureException("Authentication failed");
    }
}