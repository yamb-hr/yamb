package com.tejko.yamb.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.enums.LogLevel;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.interfaces.services.LogService;
import com.tejko.yamb.security.AuthContext;

@Component
public class YambLogger {

    private final LogService logService;
    private final AuthContext authContext;

    @Autowired
    public YambLogger(AuthContext authContext, LogService logService) {
        this.authContext = authContext;
        this.logService = logService;
    }

    public void log(String message, Object data, LogLevel logLevel) {
        try {
            Log log = Log.getInstance(null, message, logLevel, data);
            if (logLevel == LogLevel.ERROR) {
                logService.create(log);
                System.out.println(log);
            }
            
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void log(String message) {
        log(message, null, LogLevel.INFO);
    }

    public void log(String message, Object data) {
        log(message, data, LogLevel.INFO);
    }

    public void error(Exception exception) {
        log(exception.getMessage(), exception.getClass(), LogLevel.ERROR);
    }

}
