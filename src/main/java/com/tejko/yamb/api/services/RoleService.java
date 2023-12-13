package com.tejko.yamb.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.tejko.yamb.models.Role;
import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.interfaces.RestService;
import com.tejko.yamb.repositories.RoleRepository;

@Service
public class RoleService implements RestService<Role> {

    @Autowired
    RoleRepository RoleRepo;

    public Role getById(Long id) {
        return RoleRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
    }

    public List<Role> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return RoleRepo.findAll(pageable).getContent();
    }

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
    
}