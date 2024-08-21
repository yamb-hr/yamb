package com.tejko.yamb.domain.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tejko.yamb.interfaces.BaseRepository;
import com.tejko.yamb.domain.models.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>, BaseRepository<Player> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);
    @Query(value = "SELECT GREATEST(" +
                    "COALESCE((SELECT MAX(s.created_at) FROM score s WHERE s.player_id = :playerId), '1970-01-01'), " +
                    "COALESCE((SELECT MAX(g.updated_at) FROM game g WHERE g.player_id = :playerId), '1970-01-01'))", 
        nativeQuery = true)
    LocalDateTime findLastActivityByPlayerId(@Param("playerId") Long playerId);



}
