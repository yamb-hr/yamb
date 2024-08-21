package com.tejko.yamb.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tejko.yamb.interfaces.BaseRepository;
import com.tejko.yamb.domain.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long>, BaseRepository<Score> {

    List<Score> findAllByPlayerIdOrderByCreatedAtDesc(Long playerId);

    List<Score> findTop30ByCreatedAtBetweenOrderByValueDesc(LocalDateTime from, LocalDateTime to);

    List<Score> findTop30ByOrderByValueDesc();

    @Query("SELECT AVG(s.value) FROM Score s WHERE s.player.id = :playerId")
    Double findAverageValueByPlayerId(Long playerId);

    @Query("SELECT MAX(s.value) FROM Score s WHERE s.player.id = :playerId")
    Integer findTopValueByPlayerId(Long playerId);

    long countByPlayerId(Long playerId);

    @Query("SELECT AVG(s.value) FROM Score s")
    Double findAverageValue();

}
