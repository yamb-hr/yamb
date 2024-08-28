package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.domain.models.Game;

public interface GameRepository extends MongoRepository<Game, String> {
    
    Optional<Game> findByPlayerIdAndStatusIn(Long playerId, List<GameStatus> statuses);

    boolean existsByPlayerIdAndStatusIn(Long playerId, List<GameStatus> statuses);

}