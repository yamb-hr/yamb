package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tejko.yamb.domain.models.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByExternalId(UUID externalId);

    Optional<Player> findByUsername(String username);

    Optional<Player> findByEmail(String email);

    Optional<Player> findByEmailVerificationToken(String token);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCaseAndEmailVerified(String email, boolean emailVerified);
    
    Optional<Player> findTop1ByOrderByCreatedAtDesc();

    Optional<Player> findTop1ByOrderByCreatedAtAsc();

    @Query("SELECT p FROM player p JOIN p.scores s GROUP BY p.id ORDER BY COUNT(s) DESC")
    Page<Player> findPlayerWithMostScores(Pageable pageable);

    @Query("SELECT p FROM player p JOIN p.scores s GROUP BY p.id ORDER BY AVG(s.value) DESC")
    Page<Player> findPlayerWithHighestAverageScore(Pageable pageable);
    
    List<Player> findAllByOrderByCreatedAtDesc();

    List<Player> findAllByExternalIdInOrderByUpdatedAtDesc(List<UUID> externalIds);

    void deleteByExternalId(UUID externalId);

}
