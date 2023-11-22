package com.tejko.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.models.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

}
