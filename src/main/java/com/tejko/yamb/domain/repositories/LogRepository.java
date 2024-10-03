package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.domain.models.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

    Optional<Log> findByExternalId(UUID externalId);

    List<Log> findAllByOrderByCreatedAtDesc();

    List<Log> findAllByPlayerId(Long playerId);
    
}
