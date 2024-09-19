package com.tejko.yamb.business.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.LogService;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.repositories.LogRepository;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepo;

    @Autowired
    public LogServiceImpl(LogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @Override
    public Log getByExternalId(UUID externalId) {
        Log log = logRepo.findByExternalId(externalId).orElseThrow(() -> new ResourceNotFoundException());
        return log;
    }

    @Override
    public Page<Log> getAll(Pageable pageable) {
        return logRepo.findAll(pageable);
    }

    @Override
    public Log create(Log log) {
        logRepo.save(log);
        return log;
    }

    @Override
    public void deleteAll() {
        logRepo.deleteAll();
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        logRepo.deleteByExternalId(externalId);
    }

}