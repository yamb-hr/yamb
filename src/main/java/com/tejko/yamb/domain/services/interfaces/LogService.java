package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import com.tejko.yamb.api.dto.requests.LogRequest;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.domain.models.Log;

public interface LogService {

    public Log fetchById(Long id);

    public LogResponse getById(Long id);

    public List<LogResponse> getAll();
    
    public LogResponse create(LogRequest logRequest);

    public void deleteById(Long id);

    public void deleteAll();
    
}
