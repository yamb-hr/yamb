package com.tejko.yamb.logging;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.event.Level;

import com.tejko.yamb.business.interfaces.LogService;
import com.tejko.yamb.domain.models.Log;
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
        if (!eventObject.getLevel().isGreaterOrEqual(ch.qos.logback.classic.Level.ERROR)) {
            return;
        }
        try {
            IThrowableProxy throwableProxy = eventObject.getThrowableProxy();
            if (throwableProxy != null) {
                StackTraceElementProxy[] stackTraceElementProxies = throwableProxy.getStackTraceElementProxyArray();

                String stackTraceData = objectMapper.writeValueAsString(
                        Arrays.stream(stackTraceElementProxies)
                                .map(StackTraceElementProxy::getStackTraceElement)
                                .filter(element -> element.getClassName().startsWith("com.tejko"))
                                .map(StackTraceElement::toString)
                                .collect(Collectors.toList())
                );
                String message = eventObject.getThrowableProxy().getMessage() != null ? eventObject.getThrowableProxy().getMessage() : eventObject.getThrowableProxy().getClassName();
                logService.create(Log.getInstance(AuthContext.getAuthenticatedPlayer(), message, stackTraceData, Level.ERROR));
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}
