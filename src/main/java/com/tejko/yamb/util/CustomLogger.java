package com.tejko.yamb.util;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.api.dto.requests.LogRequest;
import com.tejko.yamb.domain.services.interfaces.LogService;

@Component
public class CustomLogger {

    private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());

    private final LogService logService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomLogger(LogService logService, ObjectMapper objectMapper) {
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    public void log(String message, Object data, Level level) {
        logger.log(level, message);
        logService.create(new LogRequest(message, data, level.getName()));
    }

    public void log(String message) {
        log(message, null, Level.INFO);
    }

    public void log(String message, Object data) {
        log(message, data, Level.INFO);
    }

    public void error(Exception exception) {
        String message = exception.getLocalizedMessage();
        String data = exception.getClass().getName();
        try {
            data = objectMapper.writeValueAsString(
                Arrays.stream(exception.getStackTrace())
                    .map(StackTraceElement::toString)
                    .toArray(String[]::new));
        } catch (Exception e) {
            System.err.println("Failed to serialize stack trace: " + e.getMessage());
        } finally {
            log(message, data, Level.SEVERE);
        }
    }
}
