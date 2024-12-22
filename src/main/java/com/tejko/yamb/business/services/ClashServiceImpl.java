package com.tejko.yamb.business.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.ClashService;
import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.enums.GameType;
import com.tejko.yamb.domain.enums.NotificationType;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Notification;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Clash.ClashPlayer;
import com.tejko.yamb.domain.repositories.ClashRepository;
import com.tejko.yamb.domain.repositories.GameRepository;
import com.tejko.yamb.domain.repositories.NotificationRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.security.AuthContext;

@Service
public class ClashServiceImpl implements ClashService {

    private final ClashRepository clashRepo;
    private final GameRepository gameRepo;
    private final PlayerRepository playerRepo;
    private final NotificationRepository notificationRepo;

    @Autowired
    public ClashServiceImpl(ClashRepository clashRepo, GameRepository gameRepo, PlayerRepository playerRepo, NotificationRepository notificationRepo) {
        this.clashRepo = clashRepo;
        this.gameRepo = gameRepo;
        this.playerRepo = playerRepo;
        this.notificationRepo = notificationRepo;
    }

    @Override
    public Clash getByExternalId(UUID externalId) {
        return clashRepo.findByExternalId(externalId).orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public Page<Clash> getAll(Pageable pageable) {
        return clashRepo.findAll(pageable);
    }

    @Override
    public Clash create(String name, UUID ownerExternalId, Set<UUID> playerExternalIds, ClashType type) {
        String playerHash = Clash.generatePlayerHash(playerExternalIds);
        if (clashRepo.findActiveByPlayerHashAndType(playerHash, type).isPresent()) {
            throw new IllegalStateException("A clash with those players already exists");
        }        
        validateCreate(playerExternalIds, type);
        Game game = Game.getInstance(ownerExternalId, GameType.CLASH);
        gameRepo.save(game);
        Clash clash = Clash.getInstance(name, ownerExternalId, playerExternalIds, type);
        clash.getPlayer(ownerExternalId).setGameId(game.getExternalId());
        clashRepo.save(clash);
        List<Notification> notifications = generateClashNotifications(clash);
        notificationRepo.saveAll(notifications);
        return clash;
    }

    private List<Notification> generateClashNotifications(Clash clash) {
        
        Set<UUID> playerExternalIds = new HashSet<>();
        for (ClashPlayer player : clash.getPlayers()) {
            playerExternalIds.add(player.getId());
        }

        List<Player> players = playerRepo.findAllByExternalIdIn(playerExternalIds);
        List<Notification> notifications = new ArrayList<>();
        Player owner = playerRepo.findByExternalId(clash.getOwnerId()).get();
        for (Player player : players) {
            if (!owner.getExternalId().equals(player.getExternalId())) {
                notifications.add(Notification.getInstance(player, "You have been invited to join a clash by " + owner.getUsername(), "https://jamb.com.hr/clashes/" + clash.getExternalId(), NotificationType.CLASH_INVITATION));
            }
        }

        return notifications;
    }

    private void validateCreate(Set<UUID> playerExternalIds, ClashType type) {
        Player player = AuthContext.getAuthenticatedPlayer();
        if (!playerExternalIds.contains(player.getExternalId()) && !player.isAdmin()) {
            throw new IllegalStateException("Owner must be part of the clash");
        } else if (ClashType.OFFLINE.equals(type) && clashRepo.countByPlayerIdAndStatusAndType(player.getExternalId(), ClashStatus.IN_PROGRESS, ClashType.OFFLINE) > 5) {
            throw new IllegalStateException("Clash limit reached");
        } else if (playerExternalIds.size() > 4 || playerExternalIds.size() < 2) {
            throw new IllegalStateException("Number of players must be between 2 and 4");
        }
        for (UUID playerExternalId : playerExternalIds) {
            if (playerExternalId == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public List<Clash> getByPlayerExternalId(UUID playerExternalId) {
        return clashRepo.findAllByPlayerIdOrderByUpdatedAtDesc(playerExternalId);
    }

    @Override
    public Clash acceptInvitationByExternalId(UUID externalId, UUID playerExternalId) {
        Clash clash = getByExternalId(externalId);
        Game game = Game.getInstance(playerExternalId, GameType.CLASH);
        gameRepo.save(game);
        clash.acceptInvitation(playerExternalId, game.getExternalId());
        clash.getPlayer(playerExternalId).setGameId(game.getExternalId());
        clashRepo.save(clash);
        return clash;
    }

    @Override
    public Clash declineInvitationByExternalId(UUID externalId, UUID playerExternalId) {
        Clash clash = getByExternalId(externalId);
        clash.declineInvitation(playerExternalId);
        return clashRepo.save(clash);
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        Clash clash = getByExternalId(externalId);
        clashRepo.delete(clash);
    }

    @Override
    public void deleteAll() {
        clashRepo.deleteAll();
    }
    
}
