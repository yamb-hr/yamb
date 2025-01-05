package com.tejko.yamb.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class TicketCodeSequenceIdentifier implements IdentifierGenerator {

    private static final String PREFIX = "T-";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Long nextVal = ((Number) session.createNativeQuery("SELECT nextval('ticket_code_sequence')").uniqueResult()).longValue();
        return PREFIX + nextVal;
    }

}
