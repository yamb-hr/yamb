package com.tejko.yamb.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.repositories.TicketRepository;

import java.io.Serializable;

@Component
public class TicketCodeSequenceIdentifier implements IdentifierGenerator {

    private static final String PREFIX = "T-";

    private final TicketRepository ticketRepo;

    @Autowired
    public TicketCodeSequenceIdentifier(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        try {
            String latestCode = (String) ticketRepo.getLatestCode();

            int nextCodeNumber = (latestCode != null && latestCode.startsWith(PREFIX))
                    ? Integer.parseInt(latestCode.substring(PREFIX.length())) + 1
                    : 800;

            return PREFIX + nextCodeNumber;
        } catch (Exception e) {
            throw new RuntimeException("Error generating Ticket code", e);
        }
    }
}
