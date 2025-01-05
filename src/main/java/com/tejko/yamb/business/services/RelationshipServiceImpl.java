package com.tejko.yamb.business.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.RelationshipService;
import com.tejko.yamb.domain.enums.RelationshipType;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerRelationship;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RelationshipRepository;
import com.tejko.yamb.security.AuthContext;

@Service
public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipRepository relationshipRepo;
    private final PlayerRepository playerRepo;

    @Autowired
    public RelationshipServiceImpl(RelationshipRepository relationshipRepo, PlayerRepository playerRepo) {
        this.relationshipRepo = relationshipRepo;
        this.playerRepo = playerRepo;
    }

    @Override
    public PlayerRelationship getByExternalId(UUID externalId) {
        return relationshipRepo.findByExternalId(externalId)
            .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Override
    public Page<PlayerRelationship> getAll(Pageable pageable) {
        return relationshipRepo.findAll(pageable);
    }
    
    @Override
    public PlayerRelationship requestRelationship(UUID playerExternalId, UUID relatedPlayerExternalId, RelationshipType type) {
        validateRelationshipRequest(playerExternalId,relatedPlayerExternalId,type);
        Player player = playerRepo.findByExternalId(playerExternalId).get();
        Player relatedPlayer = playerRepo.findByExternalId(relatedPlayerExternalId).get();
        Optional<PlayerRelationship> existingRelationship = relationshipRepo.findByPlayerIds(player.getId(), relatedPlayer.getId());
        PlayerRelationship relationship;
        if (existingRelationship.isPresent()) {
            relationship = existingRelationship.get();
            if (type == RelationshipType.FRIEND && relationship.getType() == RelationshipType.FRIEND && player.getId() == relationship.getId().getRelatedPlayer().getId() && !relationship.isActive()) {
                relationship.setActive(true);
                relationshipRepo.save(relationship);
            } else if (RelationshipType.BLOCK.equals(type)) {
                relationshipRepo.delete(relationship);
                relationship = PlayerRelationship.getInstance(player, relatedPlayer, type);
                relationship.setActive(true);
            }
        } else {
            relationship = PlayerRelationship.getInstance(player, relatedPlayer, type);
            if (RelationshipType.BLOCK.equals(type)) {
                relationship.setActive(true);
            }
        }
        relationshipRepo.save(relationship);
        return relationship;
    }

    private void validateRelationshipRequest(UUID playerExternalId, UUID relatedPlayerExternalId, RelationshipType type) {
        
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer();
        if (playerExternalId != authenticatedPlayer.getExternalId()) {
            throw new IllegalStateException();
        } else if (playerExternalId == relatedPlayerExternalId) {
            throw new IllegalStateException();
        }

        Player player = playerRepo.findByExternalId(playerExternalId).get();
        Player relatedPlayer = playerRepo.findByExternalId(relatedPlayerExternalId).get();
        Optional<PlayerRelationship> existingRelationship = relationshipRepo.findByPlayerIds(player.getId(), relatedPlayer.getId());

        if (existingRelationship.isPresent() && existingRelationship.get().getType() == RelationshipType.BLOCK) {
            throw new IllegalStateException();
        } else if (existingRelationship.isPresent() && existingRelationship.get().getType() == RelationshipType.FRIEND && existingRelationship.get().isActive()) {
            throw new IllegalStateException();
        } else if (existingRelationship.isPresent() && existingRelationship.get().getType() == RelationshipType.FRIEND && !existingRelationship.get().isActive() && player.getId() == existingRelationship.get().getId().getPlayer().getId()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public PlayerRelationship acceptByExternalId(UUID externalId) {
        validateAcceptRelationship(externalId);
        PlayerRelationship relationship = getByExternalId(externalId);
        relationship.setActive(true);
        relationshipRepo.save(relationship);
        return relationship;
    }

    private void validateAcceptRelationship(UUID externalId) {
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer();
        PlayerRelationship relationship = getByExternalId(externalId);
        if (authenticatedPlayer.getId() != relationship.getId().getRelatedPlayer().getId()) {
            throw new IllegalStateException();
        } else if (relationship.getType() == RelationshipType.BLOCK) {
            throw new IllegalStateException();
        } else if (relationship.isActive()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void declineByExternalId(UUID externalId) {
        validateDeclineRelationship(externalId);
        PlayerRelationship relationship = getByExternalId(externalId);
        relationshipRepo.delete(relationship);
    }

    private void validateDeclineRelationship(UUID externalId) {
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer();
        PlayerRelationship relationship = getByExternalId(externalId);
        if (authenticatedPlayer.getId() != relationship.getId().getRelatedPlayer().getId()) {
            throw new IllegalStateException();
        } else if (relationship.getType() == RelationshipType.BLOCK) {
            throw new IllegalStateException();
        } else if (relationship.isActive()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        validateDeleteRelationship(externalId);
        PlayerRelationship relationship = getByExternalId(externalId);
        relationshipRepo.delete(relationship);
    }

    private void validateDeleteRelationship(UUID externalId) {
        Player authenticatedPlayer = AuthContext.getAuthenticatedPlayer();
        PlayerRelationship relationship = getByExternalId(externalId);
        if (relationship.getType() == RelationshipType.FRIEND && (authenticatedPlayer.getId() == relationship.getId().getPlayer().getId() || authenticatedPlayer.getId() == relationship.getId().getRelatedPlayer().getId())) {
            throw new IllegalStateException();
        } else if (relationship.getType() == RelationshipType.BLOCK && authenticatedPlayer.getId() != relationship.getId().getPlayer().getId() ) {
            throw new IllegalStateException();
        }
    }
    
}
