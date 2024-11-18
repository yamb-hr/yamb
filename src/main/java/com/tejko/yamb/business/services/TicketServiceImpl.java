package com.tejko.yamb.business.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.TicketService;
import com.tejko.yamb.domain.models.Ticket;
import com.tejko.yamb.domain.repositories.TicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
        
    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Override
    public Ticket getByExternalId(UUID externalId) {
        Ticket ticket = ticketRepo.findByExternalId(externalId).orElseThrow(() -> new ResourceNotFoundException());
        return ticket;
    }

    @Override
    public Page<Ticket> getAll(Pageable pageable) {
        return ticketRepo.findAll(pageable);
    }

    @Override
    public Ticket create(Ticket ticket) {
        ticketRepo.save(ticket);
        return ticket;
    }
    
    @Override
    public Ticket patchByExternalId(UUID externalId, Map<String, Object> updates) {

        Ticket existingTicket = getByExternalId(externalId);

        updates.forEach((fieldName, fieldValue) -> {
            try {
                var field = Ticket.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(existingTicket, fieldValue);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Invalid field name: " + fieldName, e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to set field: " + fieldName, e);
            }
        });

        return ticketRepo.save(existingTicket);
    }

    @Override
    public void deleteAll() {
        ticketRepo.deleteAll();
    }

}