package com.tejko.yamb.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.domain.services.interfaces.LogService;

@Component
public class LogbackAppenderListener implements ApplicationListener<ApplicationReadyEvent> {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    public LogbackAppenderListener(LogService logService, ObjectMapper objectMapper) {
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        DatabaseAppender databaseAppender = new DatabaseAppender(logService, objectMapper);
        databaseAppender.setContext(context);
        databaseAppender.start();

        context.getLogger("ROOT").addAppender(databaseAppender);

        // Optional: Print internal state to ensure it's configured correctly
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}
