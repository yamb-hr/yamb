package com.tejko.yamb.business.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.models.Log;

public interface LogService {

    Log getByExternalId(UUID externalId);

    Page<Log> getAll(Pageable pageable);
    
    Log create(Log log);

    void deleteAll();
    
}
