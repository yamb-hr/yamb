package com.tejko.yamb.websocket;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.tejko.yamb.domain.enums.PlayerStatus;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private static final String PLAYER_STATUS_KEY = "playerStatus";

    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, UUID, PlayerStatus> hashOps;

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    public CustomWebSocketHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOps = redisTemplate.opsForHash();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("here");
        UUID playerExternalId = getPlayerExternalIdFromSession(session);
        // sessions.put(username, session);
        // setPlayerStatus(username, );
        System.out.println(playerExternalId + " connected");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UUID playerExternalId = getPlayerExternalIdFromSession(session);
        System.out.println("Message from user: " + playerExternalId + ": " + message.getPayload());
        // setPlayerStatus(username, PlayerStatus.ONLINE);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UUID playerExternalId = getPlayerExternalIdFromSession(session);
        // sessions.remove(username);
        // removePlayerStatus(username);
        System.out.println(playerExternalId + " disconnected");
    }

    private UUID getPlayerExternalIdFromSession(WebSocketSession session) {
        return UUID.fromString(session.getAttributes().get("playerId").toString());
    }

    // private void setPlayerStatus(UUID playerId, PlayerStatus status) {
    //     hashOps.put(PLAYER_STATUS_KEY, playerId, status);
    // }

    // private PlayerStatus getPlayerStatus(UUID playerId) {
    //     return hashOps.get(PLAYER_STATUS_KEY, playerId);
    // }
    
    // private Map<UUID, PlayerStatus> getAllPlayerStatuses() {
    //     return hashOps.entries(PLAYER_STATUS_KEY);
    // }

    // private void removePlayerStatus(UUID playerId) {
    //     hashOps.delete(PLAYER_STATUS_KEY, playerId);
    // }
}
