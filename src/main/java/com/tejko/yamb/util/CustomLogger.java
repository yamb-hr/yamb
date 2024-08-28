package com.tejko.yamb.util;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.lang.System.Logger;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.api.dto.requests.LogRequest;
import com.tejko.yamb.domain.services.interfaces.LogService;

@Component
public class CustomLogger implements Logger {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CustomLogger(LogService logService, ObjectMapper objectMapper) {
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    public void error(Exception exception) {
        String message = exception.getLocalizedMessage();
        try {
            String data = objectMapper.writeValueAsString(
                Arrays.stream(exception.getStackTrace())
                    .filter(element -> element.getClassName().startsWith("com.tejko"))
                    .map(StackTraceElement::toString)
                    .toArray(String[]::new));
            logService.create(new LogRequest(message, data, Level.ERROR));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while saving log: " + e.getLocalizedMessage());
        } finally {
            log(Level.ERROR, null, message, exception);
        }
    }

    @Override
    public String getName() {
        return "CustomLogger";
    }

    @Override
    public boolean isLoggable(Level level) {
        return true;
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String message, Throwable thrown) {
        System.out.println("\n-> " + LocalDateTime.now() + " " + level + " " + getName() + ": " + message + "\n");
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        System.out.println("\n-> " + LocalDateTime.now() + " " + level + " " + getName() + ": " + format + "\n");
    }
}
