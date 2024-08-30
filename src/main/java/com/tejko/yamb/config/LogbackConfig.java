package com.tejko.yamb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tejko.yamb.domain.services.interfaces.LogService;
import com.tejko.yamb.util.DatabaseAppender;

@Configuration
public class LogbackConfig {

    @Bean
    public DatabaseAppender databaseAppender(LogService logService, ObjectMapper objectMapper) {
        DatabaseAppender databaseAppender = new DatabaseAppender(logService, objectMapper);
        databaseAppender.start();
        return databaseAppender;
    }
}
