package com.tejko.yamb.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tejko.yamb.domain.enums.TicketStatus;
import com.tejko.yamb.domain.models.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByExternalId(UUID externalId);

    List<Ticket> findAllByOrderByUpdatedAtDesc();

    List<Ticket> findAllByPlayerIdOrderByUpdatedAtDesc(Long playerId);

    List<Ticket> findAllByStatusOrderByUpdatedAtDesc(TicketStatus Status);

    @Query(value = "SELECT code FROM ticket ORDER BY code DESC LIMIT 1", nativeQuery = true)
    String getLatestCode();
    
}
