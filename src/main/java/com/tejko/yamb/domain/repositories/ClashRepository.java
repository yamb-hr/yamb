package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.models.Clash;

public interface ClashRepository extends MongoRepository<Clash, String> {

    Optional<Clash> findByExternalId(UUID externalId);

    @Query("{ 'players.id': ?0 }")
    List<Clash> findAllByPlayerId(UUID playerId);
    
    @Query("{ 'players.id': { $in: ?0 } }")
    List<Clash> findAllByPlayerIdIn(Set<UUID> playerIds);

    @Query("{ 'players.id': ?0 }")
    List<Clash> findAllByPlayerIdOrderByUpdatedAtDesc(UUID playerId);

    @Query("{ 'players.id': ?0, 'status': ?1, 'type': ?2 }")
    boolean existsByPlayerIdAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);

    @Query("{ 'players.id': ?0, 'status': ?1, 'type': ?2 }")
    Optional<Clash> findByPlayerIdAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);

    @Query("{ 'playerHash': ?0, 'status': { $in: ['PENDING', 'IN_PROGRESS'] }, 'type': ?1 }")
    Optional<Clash> findActiveByPlayerHashAndType(String playerHash, ClashType type);

    @Query("{ 'players.id': ?0, 'status': ?1, 'type': ?2 }")
    int countByPlayerIdAndStatusAndType(UUID playerId, ClashStatus status, ClashType type);
    
    @Query("{ 'players.gameId': ?0 }")
    Optional<Clash> findByGameId(UUID gameId);

    List<Clash> findAllByOrderByUpdatedAtDesc();
    
}
