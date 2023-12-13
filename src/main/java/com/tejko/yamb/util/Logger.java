package com.tejko.yamb.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.services.LogService;
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

    public void log(String message) {
        try {
            logService.create(Log.getInstance(null, message, LogLevel.DEBUG, null));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void error(Exception exception) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(username);
            Optional<Player> player = playerRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (player.isPresent()) {
                logService.create(Log.getInstance(player.get(), exception.getMessage(), LogLevel.ERROR, null));        
            }
            logService.create(Log.getInstance(null, exception.getMessage(), LogLevel.ERROR, null));        
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

}
