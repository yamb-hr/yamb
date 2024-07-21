package com.tejko.yamb.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.interfaces.BaseRepository;
import com.tejko.yamb.domain.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long>, BaseRepository<Score> {

    List<Score> findAllByPlayerId(Long playerId);

    List<Score> findTop15ByCreatedAtBetweenOrderByValueDesc(LocalDateTime from, LocalDateTime to);

    List<Score> findTop15ByOrderByValueDesc();

}
