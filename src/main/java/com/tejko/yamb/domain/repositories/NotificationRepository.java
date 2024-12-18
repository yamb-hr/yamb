package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.domain.models.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByExternalId(UUID externalId);

    List<Notification> findAllByPlayerIdOrderByCreatedAtDesc(Long playerId);

    void deleteByExternalId(UUID externalId);

    void deleteAllByPlayerId(Long playerId);
    
}
