package com.tejko.yamb.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.enums.GameStatus;
import com.tejko.yamb.interfaces.BaseRepository;

public interface GameRepository extends JpaRepository<Game, Long>, BaseRepository<Game> {

    Game existsByPlayerIdAndStatus(Long playerId, GameStatus status);
    
    Optional<Game> findByPlayerIdAndStatus(Long playerId, GameStatus status);

}
