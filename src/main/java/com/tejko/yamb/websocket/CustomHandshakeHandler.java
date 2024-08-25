package com.tejko.yamb.websocket;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.tejko.yamb.util.JwtUtil;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtUtil jwtUtil;

    @Autowired
    public CustomHandshakeHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        Optional<String> token = Optional.ofNullable(
            UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("token")
        );

        return token.filter(jwtUtil::validateToken)
            .flatMap(jwtUtil::extractIdFromToken)
            .map(StompPrincipal::new)
            .orElseThrow(() -> new HandshakeFailureException("Authentication failed"));
    }
}
