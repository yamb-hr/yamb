package com.tejko.yamb.business.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.models.Clash;

public interface ClashService {

    Clash getByExternalId(UUID externalId);

    Page<Clash> getAll(Pageable pageable);

    Clash create(String name, UUID ownerExternalId, Set<UUID> playerExternalIds, ClashType type);
    
    List<Clash> getByPlayerExternalId(UUID playerExternalId);

    Clash acceptInvitationByExternalId(UUID externalId, UUID playerExternalId);

    Clash declineInvitationByExternalId(UUID externalId, UUID playerExternalId);

    void deleteByExternalId(UUID externalId);
    
    void deleteAll();

}
