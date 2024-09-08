package com.tejko.yamb.business.interfaces;

import java.util.List;

import com.tejko.yamb.domain.models.entities.Log;

public interface LogService {

    public Log getById(Long id);

    public List<Log> getAll();
    
    public Log create(Log log);

    public void deleteById(Long id);

    public void deleteAll();
    
}
