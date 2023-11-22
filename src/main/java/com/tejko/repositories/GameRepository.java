package com.tejko.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.models.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
