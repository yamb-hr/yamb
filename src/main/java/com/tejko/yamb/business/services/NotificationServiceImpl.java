package com.tejko.yamb.business.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.NotificationService;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.domain.repositories.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    @Override
    public Optional<Notification> findByExternalId(UUID externalId) {
        return notificationRepo.findByExternalId(externalId);
    }

    @Override
    public Notification getByExternalId(UUID externalId) {
        return findByExternalId(externalId)
            .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        notificationRepo.deleteByExternalId(externalId);
    }
    
}
