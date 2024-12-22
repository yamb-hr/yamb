package com.tejko.yamb.business.interfaces;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.enums.NotificationType;
import com.tejko.yamb.domain.models.Notification;

public interface NotificationService {
    
    Page<Notification> getAll(Pageable pageable);

    Notification create(UUID playerExternalId, String content, String link, NotificationType type);

    Optional<Notification> findByExternalId(UUID externalId);

    Notification getByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);
    
    void deleteAll();
    
}
