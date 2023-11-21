package com.tejko.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.models.Game;

public interface GameRepository extends JpaRepository<Game, UUID> {

    public List<Game> findAllByPlayerId(UUID playerId);

}
