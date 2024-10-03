package com.tejko.yamb.business.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.enums.ClashType;
import com.tejko.yamb.domain.models.Clash;

public interface ClashService {

    public Clash getByExternalId(UUID externalId);

    public Page<Clash> getAll(Pageable pageable);

    public Clash getOrCreate(List<UUID> playerExternalIds, ClashType type);
    
    public List<Clash> getByPlayerExternalId(UUID playerExternalId);

    public Clash acceptInvitationByExternalId(UUID externalId);

    public Clash declineInvitationByExternalId(UUID externalId);

    public void deleteByExternalId(UUID externalId);

}
