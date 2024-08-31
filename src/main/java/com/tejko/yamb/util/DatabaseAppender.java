package com.tejko.yamb.util;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.services.interfaces.LogService;
import com.tejko.yamb.security.AuthContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseAppender extends AppenderBase<ILoggingEvent> {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    public DatabaseAppender(LogService logService, ObjectMapper objectMapper) {
        this.logService = logService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        try {
            String message = eventObject.getFormattedMessage();
            String stackTraceData = null;

            IThrowableProxy throwableProxy = eventObject.getThrowableProxy();

            if (throwableProxy != null) {
                StackTraceElementProxy[] stackTraceElementProxies = throwableProxy.getStackTraceElementProxyArray();

                stackTraceData = objectMapper.writeValueAsString(
                        Arrays.stream(stackTraceElementProxies)
                                .map(StackTraceElementProxy::getStackTraceElement)
                                .filter(element -> element.getClassName().startsWith("com.tejko"))
                                .map(StackTraceElement::toString)
                                .collect(Collectors.toList())
                );
                
                logService.create(Log.getInstance(AuthContext.getAuthenticatedPlayer().orElse(null), message, stackTraceData, Level.ERROR));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to log to database: " + e.getLocalizedMessage());
        }
    }
}
