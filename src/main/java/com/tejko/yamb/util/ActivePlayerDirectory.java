package com.tejko.yamb.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.tejko.yamb.domain.enums.PlayerStatus;

public class ActivePlayerDirectory {
    
    private static final Map<UUID, PlayerStatus> playerStatusMap = new ConcurrentHashMap<>();

    public static void setPlayerStatus(UUID playerExternalId, PlayerStatus status) {
        playerStatusMap.put(playerExternalId, status);
    }

    public static Map<UUID, PlayerStatus> getPlayerStatusMap() {
        return playerStatusMap;
    }

    public static Set<UUID> getActivePlayerExternalIdSet() {
        Set<UUID> activePlayerExternalIdSet = new HashSet<>();
        for (UUID playerExternalId : playerStatusMap.keySet()) {
            if (PlayerStatus.ONLINE.equals(playerStatusMap.get(playerExternalId))) {
                activePlayerExternalIdSet.add(playerExternalId);
            }
        }
        return activePlayerExternalIdSet;
    }
    
}
