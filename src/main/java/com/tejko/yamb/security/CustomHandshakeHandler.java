package com.tejko.yamb.security;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.lang.NonNull;
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
    protected Principal determineUser(@NonNull ServerHttpRequest request, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        Optional<String> token = Optional.ofNullable(
            UriComponentsBuilder.fromUri(request.getURI())
                .build()
                .getQueryParams()
                .getFirst("token")
        );

        return token.filter(jwtUtil::validateToken)
            .flatMap(jwtUtil::extractExternalIdFromToken)
            .map(externalId -> {
                String principal = UUID.randomUUID().toString();
                webSocketService.addPrincipal(externalId, principal);
                return new StompPrincipal(principal);
            })
            .orElseThrow(() -> new HandshakeFailureException("Authentication failed"));
    }
}
