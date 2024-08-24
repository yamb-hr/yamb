package com.tejko.yamb.domain.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tejko.yamb.domain.models.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT MAX(s.created_at) FROM score s WHERE s.player_id = :playerId", nativeQuery = true)
    LocalDateTime findLastActivityByPlayerId(@Param("playerId") Long playerId);

}
