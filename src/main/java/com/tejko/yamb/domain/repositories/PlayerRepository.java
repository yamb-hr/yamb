package com.tejko.yamb.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.interfaces.BaseRepository;
import com.tejko.yamb.domain.models.Player;

public interface PlayerRepository extends JpaRepository<Player, Long>, BaseRepository<Player> {

    Optional<Player> findByUsername(String username);

    boolean existsByUsername(String username);

}
