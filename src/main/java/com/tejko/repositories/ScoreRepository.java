package com.tejko.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.models.Score;

public interface ScoreRepository extends JpaRepository<Score, UUID> {

    List<Score> findAllByPlayerId(UUID playerId);

}
