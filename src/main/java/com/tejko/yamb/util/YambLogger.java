package com.tejko.yamb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.enums.LogLevel;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.interfaces.services.LogService;

@Component
public class YambLogger {

    @Autowired
    LogService logService;

    @Autowired
    PlayerRepository playerRepo;

    private YambLogger() {}

    public static YambLogger getInstance() {
        return new YambLogger();
    }

    public void log(String message, Object data, LogLevel logLevel) {
        try {
            Player player = playerRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
            Log log = Log.getInstance(player, message, logLevel, data);
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
