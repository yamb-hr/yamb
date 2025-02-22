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
import com.tejko.yamb.util.TokenExtractor;

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
        String authToken = TokenExtractor.extractToken(request);
        if (authToken != null && jwtUtil.validateToken(authToken)) {
            UUID playerExternalId = jwtUtil.getPlayerExternalIdFromToken(authToken);
            Principal principal = playerRepo.findByExternalId(playerExternalId)
                .map(player -> (Principal) player)
                .orElseThrow(() -> new HandshakeFailureException("Player not found"));
            return principal;
        }
        throw new HandshakeFailureException("Invalid or missing token");
    }

    
    
}