package com.tejko.yamb.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.models.Log;
import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.interfaces.RestService;
import com.tejko.yamb.repositories.LogRepository;

@Service
public class LogService implements RestService<Log> {

    @Autowired
    LogRepository logRepo;

    @Override
    public Log getById(Long id) {
        return logRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(MessageConstants.ERROR_LOG_NOT_FOUND));
    }

    @Override
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

    @Override
    public void deleteById(Long id) {
        logRepo.deleteById(id);
    }

}