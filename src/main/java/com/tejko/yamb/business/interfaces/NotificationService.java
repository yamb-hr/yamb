package com.tejko.yamb.business.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.tejko.yamb.domain.models.Notification;

public interface NotificationService {

    Optional<Notification> findByExternalId(UUID externalId);

    Notification getByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);
    
}
