package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.models.Clash;

public interface ClashRepository extends MongoRepository<Clash, String> {
    
    Optional<Clash> findByExternalId(UUID externalId);
    
    List<Clash> findAllByPlayerIdsContains(UUID playerId);
    
    boolean existsByPlayerIdsContainsAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);

    Optional<Clash> findByPlayerIdsContainsAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);

    boolean existsByPlayerIdsAndStatusAndType(List<UUID> playerIds, ClashStatus status, ClashType type);

    Optional<Clash> findByPlayerIdsAndStatusAndType(List<UUID> playerIds, ClashStatus status, ClashType type);
    
    int countByPlayerIdsContainsAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);

    Optional<Clash> findByCurrentPlayerIdAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);

    List<Clash> findAllByOrderByUpdatedAtDesc();
    
    void deleteByExternalId(UUID externalId);

}