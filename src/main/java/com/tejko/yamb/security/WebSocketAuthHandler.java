package com.tejko.yamb.security;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.util.JwtUtil;

@Component
public class WebSocketAuthHandler extends DefaultHandshakeHandler {

    private final JwtUtil jwtUtil;
    private final PlayerRepository playerRepo;

    @Autowired
    public WebSocketAuthHandler(JwtUtil jwtUtil, PlayerRepository playerRepo) {
        this.jwtUtil = jwtUtil;
        this.playerRepo = playerRepo;
    }

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        String authToken = parseToken(request);
        if (authToken != null && jwtUtil.validateToken(authToken)) {
            UUID playerExternalId = jwtUtil.getPlayerExternalIdFromToken(authToken);
            return playerRepo.findByExternalId(playerExternalId).orElse(null);
        }
        throw new HandshakeFailureException("error.handshake_failed");
    }

    private String parseToken(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2 && pair[0].equals("token")) {
                    return pair[1];
                }
            }
        }
        return null;
    }
    
}