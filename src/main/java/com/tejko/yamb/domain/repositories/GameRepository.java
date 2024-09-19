package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Game;

public interface GameRepository extends MongoRepository<Game, String> {
    
    Optional<Game> findByPlayerIdAndStatusIn(UUID playerId, List<GameStatus> statuses);

    boolean existsByPlayerIdAndStatusIn(UUID playerId, List<GameStatus> statuses);

    List<Game> findAllByOrderByUpdatedAtDesc();    

}