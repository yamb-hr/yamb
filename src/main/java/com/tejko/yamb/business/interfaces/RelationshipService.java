package com.tejko.yamb.business.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.enums.RelationshipType;
import com.tejko.yamb.domain.models.PlayerRelationship;

public interface RelationshipService {

    Page<PlayerRelationship> getAll(Pageable pageable);  

    PlayerRelationship getByExternalId(UUID externalId);

    PlayerRelationship requestRelationship(UUID playerExternalId, UUID relatedPlayerExternalId, RelationshipType type);

    PlayerRelationship acceptByExternalId(UUID externalId);

    void declineByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);

    void deleteAll();
    
}
