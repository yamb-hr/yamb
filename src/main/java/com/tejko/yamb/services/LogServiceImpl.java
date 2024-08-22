package com.tejko.yamb.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.services.LogService;
import com.tejko.yamb.domain.repositories.LogRepository;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepo;

    public Log getByExternalId(UUID externalId) {
        return logRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_LOG_NOT_FOUND));
    }

    public List<Log> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return logRepo.findAll(pageable).getContent();
    }

    public Log create(Log log) {
        return logRepo.save(log);
    }

    public void deleteAll() {
        logRepo.deleteAll();
    }

    public void deleteByExternalId(UUID externalId) {
        logRepo.deleteByExternalId(externalId);
    }

}