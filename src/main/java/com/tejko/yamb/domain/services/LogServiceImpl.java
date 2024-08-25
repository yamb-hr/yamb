package com.tejko.yamb.domain.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.requests.LogRequest;
import com.tejko.yamb.api.dto.responses.LogResponse;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.LogRepository;
import com.tejko.yamb.domain.services.interfaces.LogService;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepo;
    private final AuthContext authContext;
    private final CustomObjectMapper mapper;

    @Autowired
    public LogServiceImpl(LogRepository logRepo, AuthContext authContext, CustomObjectMapper mapper) {
        this.logRepo = logRepo;
        this.authContext = authContext;
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
    public LogResponse create(LogRequest logRequest) {
        Player player = authContext.getAuthenticatedPlayer().orElse(null);
        Log log = Log.getInstance(player, logRequest.getMessage(), logRequest.getData(), logRequest.getLevel());
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