package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Game;

public interface GameRepository extends MongoRepository<Game, String> {
    
    Optional<Game> findByExternalId(UUID externalId);
    
    Optional<Game> findByPlayerIdAndStatusIn(UUID playerId, List<GameStatus> statuses);

    Optional<Game> findByPlayerIdAndStatus(UUID playerId, GameStatus status);

    boolean existsByPlayerIdAndStatus(UUID playerId, GameStatus status);

    boolean existsByPlayerIdAndStatusIn(UUID playerId, List<GameStatus> statuses);

    List<Game> findAllByOrderByUpdatedAtDesc();

    List<Game> findAllByPlayerIdOrderByUpdatedAtDesc(UUID playerId);
    
    void deleteByExternalId(UUID externalId);

}