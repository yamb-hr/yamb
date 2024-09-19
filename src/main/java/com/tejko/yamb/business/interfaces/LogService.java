package com.tejko.yamb.business.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tejko.yamb.domain.models.Log;

public interface LogService {

    public Log getByExternalId(UUID externalId);

    public Page<Log> getAll(Pageable pageable);
    
    public Log create(Log log);

    public void deleteByExternalId(UUID externalId);

    public void deleteAll();
    
}
