package com.tejko.yamb.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findAllByPlayerId(Long playerId);

    List<Score> findByDateBetween(LocalDateTime from, LocalDateTime to); 

}
