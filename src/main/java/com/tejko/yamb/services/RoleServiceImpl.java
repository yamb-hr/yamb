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
import com.tejko.yamb.interfaces.services.RoleService;
import com.tejko.yamb.domain.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepo;

    public Role getByExternalId(UUID externalId) {
        return roleRepo.findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
    }

    public List<Role> getAll(Integer page, Integer size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.fromString(direction), sort));
        return roleRepo.findAll(pageable).getContent();
    }

    @Override
    public void deleteByExternalId(UUID externalId) {
        roleRepo.deleteByExternalId(externalId);
    }

}