package com.tejko.yamb.business.interfaces;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.models.Ticket;

public interface TicketService {

    public Ticket getByExternalId(UUID externalId);

    public Page<Ticket> getAll(Pageable pageable);
    
    public Ticket create(Set<String> emailAddresses, String title, String description);

    public Ticket patchByExternalId(UUID externalId, Map<String, Object> updates);

    public void deleteAll();
    
}
