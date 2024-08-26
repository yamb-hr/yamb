package com.tejko.yamb.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tejko.yamb.domain.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findAllByPlayerIdOrderByCreatedAtDesc(Long playerId);

    List<Score> findTop30ByCreatedAtBetweenOrderByValueDesc(LocalDateTime from, LocalDateTime to);
    
    List<Score> findTop30ByOrderByValueDesc();

    Optional<Score> findTop1ByOrderByValueDesc();

    long countByPlayerId(Long playerId);

    Optional<Score> findTop1ByPlayerIdOrderByValueDesc(Long playerId);

    Optional<Score> findTop1ByPlayerIdOrderByCreatedAtDesc(Long playerId);
    
    @Query("SELECT AVG(s.value) FROM score s")
    Double findAverageValue();

    @Query("SELECT AVG(s.value) FROM score s WHERE s.player.id = :playerId")
    Double findAverageValueByPlayerId(Long playerId);

}
