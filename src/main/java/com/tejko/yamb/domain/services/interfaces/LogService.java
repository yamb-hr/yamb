package com.tejko.yamb.domain.services.interfaces;

import java.util.List;

import com.tejko.yamb.domain.models.Log;

public interface LogService {

    public Log getById(Long id);

    public List<Log> getAll();
    
    public Log create(Log log);

    public void deleteById(Long id);

    public void deleteAll();
    
}
