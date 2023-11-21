package com.tejko.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.models.Player;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Player findByUsername(String username);

}
