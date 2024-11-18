package com.tejko.yamb.business.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.models.Clash;

public interface ClashService {

    Clash getByExternalId(UUID externalId);

    Page<Clash> getAll(Pageable pageable);

    Clash getOrCreate(List<UUID> playerExternalIds, ClashType type);
    
    List<Clash> getByPlayerExternalId(UUID playerExternalId);

    Clash acceptInvitationByExternalId(UUID externalId);

    Clash declineInvitationByExternalId(UUID externalId);

    void deleteByExternalId(UUID externalId);

}
