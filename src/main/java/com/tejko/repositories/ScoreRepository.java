package com.tejko.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.models.Score;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
