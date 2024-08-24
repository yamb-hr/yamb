package com.tejko.yamb.interfaces.services;

import java.util.List;

import com.tejko.yamb.api.payload.responses.LogResponse;
import com.tejko.yamb.domain.models.Log;

public interface LogService {

    public Log fetchById(Long id);

    public LogResponse getById(Long id);

    public List<LogResponse> getAll();
    
    public LogResponse create(Log log);

    public void deleteById(Long id);

    public void deleteAll();
    
}
