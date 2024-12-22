package com.tejko.yamb.business.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.NotificationService;
import com.tejko.yamb.domain.enums.NotificationType;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.NotificationRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepo;
    private final PlayerRepository playerRepo;
    
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepo, PlayerRepository playerRepo) {
        this.notificationRepo = notificationRepo;
        this.playerRepo = playerRepo;
    }

    @Override
    public Page<Notification> getAll(Pageable pageable) {
        return notificationRepo.findAll(pageable);
    }

    @Override
    public Notification create(UUID playerExternalId, String content, String link, NotificationType notificationType) {
        Player player = playerRepo.findByExternalId(playerExternalId).get();
        Notification notification = Notification.getInstance(player, content, link, notificationType);
        notificationRepo.save(notification);
        return notification;
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
        Notification notification = getByExternalId(externalId);
        notificationRepo.delete(notification);
    }
    
    @Override
    public void deleteAll() {
        notificationRepo.deleteAll();
    }
    
}
