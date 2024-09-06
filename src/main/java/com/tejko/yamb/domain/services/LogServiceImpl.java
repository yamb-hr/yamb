package com.tejko.yamb.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.repositories.LogRepository;
import com.tejko.yamb.domain.services.interfaces.LogService;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepo;

    @Autowired
    public LogServiceImpl(LogRepository logRepo) {
        this.logRepo = logRepo;
    }

    @Override
    public Log getById(Long id) {
        Log log = logRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException());
        return log;
    }

    @Override
    public List<Log> getAll() {
        List<Log> logs = logRepo.findAllByOrderByCreatedAtDesc();
        return logs;
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
    public void deleteById(Long id) {
        logRepo.deleteById(id);
    }

}