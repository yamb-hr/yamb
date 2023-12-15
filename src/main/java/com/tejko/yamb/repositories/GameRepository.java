package com.tejko.yamb.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.models.Game;
import com.tejko.yamb.models.enums.GameStatus;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game existsByPlayerIdAndStatus(Long playerId, GameStatus status);
    
    Optional<Game> findByPlayerIdAndStatus(Long playerId, GameStatus status);

}
