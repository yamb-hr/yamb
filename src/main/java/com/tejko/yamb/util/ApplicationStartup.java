package com.tejko.yamb.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.RoleRepository;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepo;

    @Autowired
    public ApplicationStartup(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (roleRepo.count() == 0) {
            List<Role> roles = generateRoles();
            roleRepo.saveAll(roles);
        }
    }

    public List<Role> generateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.getInstance("ADMIN"));
        roles.add(Role.getInstance("USER"));
        return roles;
    }

}