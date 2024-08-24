package com.tejko.yamb.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.api.payload.responses.LogResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.services.LogService;
import com.tejko.yamb.util.ObjectMapper;
import com.tejko.yamb.domain.repositories.LogRepository;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepo;
    private final com.tejko.yamb.util.ObjectMapper mapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepo, ObjectMapper mapper) {
        this.logRepo = logRepo;
        this.mapper = mapper;
    }

    @Override
    public Log fetchById(Long id) {
        Log log = logRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_LOG_NOT_FOUND));
        return log;
    }

    @Override
    public LogResponse getById(Long id) {
        Log log = fetchById(id);
        return mapper.mapToResponse(log);
    }

    @Override
    public List<LogResponse> getAll() {
        List<Log> logs = logRepo.findAll();
        return logs.stream().map(mapper::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public LogResponse create(Log log) {
        logRepo.save(log);
        return mapper.mapToResponse(log);
    }

    @Override
    public void deleteAll() {
        logRepo.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        logRepo.deleteById(id);
    }

}