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

import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.interfaces.BaseService;
import com.tejko.yamb.domain.repositories.RoleRepository;

@Service
public class RoleService implements BaseService<Role> {

    @Autowired
    RoleRepository RoleRepo;

    public Role getByExternalId(UUID externalId) {
        return RoleRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
    }

    public List<Role> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return RoleRepo.findAll(pageable).getContent();
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteByExternalId'");
    }

}