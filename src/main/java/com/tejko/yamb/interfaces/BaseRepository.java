package com.tejko.yamb.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.tejko.yamb.domain.models.BaseEntity;

public interface BaseRepository<T extends BaseEntity> {

    Optional<T> findByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);

    List<T> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    List<T> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);
    
}
