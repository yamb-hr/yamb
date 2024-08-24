package com.tejko.yamb.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.enums.PlayerStatus;

@Component
public class PlayerSessionRegistry {

    private final Map<Long, PlayerStatus> playerStatusMap = new HashMap<>();

    public void updatePlayerStatus(Long id, PlayerStatus status) {
        playerStatusMap.put(id, status);
    }

    public Optional<PlayerStatus> getPlayerStatus(Long id) {
        return Optional.ofNullable(playerStatusMap.get(id));
    }

    public Map<Long, PlayerStatus> getPlayerStatusMap() {
        return new HashMap<>(playerStatusMap);
    }
    
}