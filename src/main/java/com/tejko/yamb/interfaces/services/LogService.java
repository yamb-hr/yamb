package com.tejko.yamb.interfaces.services;

import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.interfaces.BaseService;

public interface LogService extends BaseService<Log> {
    
    public Log create(Log log);

    public void deleteAll();
    
}
