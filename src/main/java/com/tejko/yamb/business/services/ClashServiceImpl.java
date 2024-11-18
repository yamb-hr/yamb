package com.tejko.yamb.business.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.ClashService;
import com.tejko.yamb.domain.enums.ClashStatus;
import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.ClashRepository;
import com.tejko.yamb.security.AuthContext;

@Service
public class ClashServiceImpl implements ClashService {

    private final ClashRepository clashRepo;

    @Autowired
    public ClashServiceImpl(ClashRepository clashRepo) {
        this.clashRepo = clashRepo;
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
    public Clash getOrCreate(List<UUID> playerExternalIds, ClashType type) {
        Optional<Clash> existingClash = clashRepo.findByPlayerIdsAndStatusAndType(playerExternalIds, ClashStatus.IN_PROGRESS, type);
        if (existingClash.isPresent()) {
            return existingClash.get();
        }        
        validateCreate(playerExternalIds, type);
        Clash clash = Clash.getInstance(AuthContext.getAuthenticatedPlayer().getExternalId(), playerExternalIds, type);
        return clashRepo.save(clash);
    }

    private void validateCreate(List<UUID> playerExternalIds, ClashType type) {
        Player player = AuthContext.getAuthenticatedPlayer();
        if (!playerExternalIds.contains(player.getExternalId())) {
            throw new IllegalStateException();
        } else if (ClashType.OFFLINE.equals(type) && clashRepo.countByPlayerIdsContainsAndStatusAndType(player.getExternalId(), ClashStatus.IN_PROGRESS, ClashType.OFFLINE) > 5) {
            throw new IllegalStateException();
        } else if (playerExternalIds.size() > 4 || playerExternalIds.size() < 2) {
            throw new IllegalStateException();
        }
    }

    @Override
    public List<Clash> getByPlayerExternalId(UUID playerExternalId) {
        return clashRepo.findAllByPlayerIdsContainsOrderByUpdatedAtDesc(playerExternalId);
    }

    @Override
    public Clash acceptInvitationByExternalId(UUID externalId) {
        Clash clash = getByExternalId(externalId);
        Player player = AuthContext.getAuthenticatedPlayer();
        clash.acceptInvitation(player.getExternalId());
        return clashRepo.save(clash);
    }

    @Override
    public Clash declineInvitationByExternalId(UUID externalId) {
        Clash clash = getByExternalId(externalId);
        Player player = AuthContext.getAuthenticatedPlayer();
        clash.declineInvitation(player.getExternalId());
        return clashRepo.save(clash);
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        clashRepo.deleteByExternalId(externalId);
    }
    
}
