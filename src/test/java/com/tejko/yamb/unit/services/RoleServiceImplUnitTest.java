package com.tejko.yamb.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.RoleRepository;
import com.tejko.yamb.services.RoleServiceImpl;


@ExtendWith(MockitoExtension.class)
public class RoleServiceImplUnitTest {

    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    private RoleServiceImpl roleService;

    private UUID externalId;
    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        externalId = UUID.randomUUID();
        role = Role.getInstance("label");
    }

    @Test
    public void testGetByExternalId_Success() {
        when(roleRepo.findByExternalId(externalId)).thenReturn(Optional.of(role));

        Role result = roleService.getByExternalId(externalId);

        assertEquals(role, result);
    }

    @Test
    public void testGetByExternalId_NotFound() {
        when(roleRepo.findByExternalId(externalId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            roleService.getByExternalId(externalId);
        });
    }

    @Test
    public void testGetAll_Success() {
        List<Role> roles = Arrays.asList(role);
        Page<Role> page = new PageImpl<>(roles);
        Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "name");
        when(roleRepo.findAll(pageable)).thenReturn(page);

        List<Role> result = roleService.getAll(0, 10, "name", "ASC");

        assertEquals(roles, result);
    }

    @Test
    public void testDeleteByExternalId_Success() {
        doNothing().when(roleRepo).deleteByExternalId(externalId);

        roleService.deleteByExternalId(externalId);

        verify(roleRepo, times(1)).deleteByExternalId(externalId);
    }

}
