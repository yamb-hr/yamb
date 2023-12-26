package com.tejko.yamb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.services.LogService;
import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.models.Log;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.models.enums.LogLevel;
import com.tejko.yamb.repositories.PlayerRepository;

@Component
public class Logger {

    @Autowired
    LogService logService;

    @Autowired
    PlayerRepository playerRepo;

    private Logger() {}

    public static Logger getInstance() {
        return new Logger();
    }

    public void log(String message, Object data, LogLevel level) {
        try {
            Player player = playerRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
            Log log = Log.getInstance(player, message, level, data);
            System.out.println(log);
            logService.create(log);
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
