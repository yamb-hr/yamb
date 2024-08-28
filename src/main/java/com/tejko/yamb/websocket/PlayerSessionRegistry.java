package com.tejko.yamb.websocket;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.enums.PlayerStatus;

@Component
public class PlayerSessionRegistry {

    private final ConcurrentMap<Long, PlayerStatus> playerStatusMap = new ConcurrentHashMap<>();

    public void updatePlayerStatus(Long playerId, PlayerStatus status) {
        playerStatusMap.put(playerId, status);
    }

    public Optional<PlayerStatus> getPlayerStatus(Long playerId) {
        return Optional.ofNullable(playerStatusMap.get(playerId));
    }

    public Map<Long, PlayerStatus> getPlayerStatusMap() {
        return new ConcurrentHashMap<>(playerStatusMap);
    }

    public void removePlayer(Long playerId) {
        playerStatusMap.remove(playerId);
    }
}
