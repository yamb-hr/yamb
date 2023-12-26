package com.tejko.yamb.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.tejko.yamb.models.Role;
import com.tejko.yamb.repositories.RoleRepository;

@Component
public class Startup implements ApplicationRunner {

    @Autowired
    RoleRepository roleRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (roleRepo.findAll().size() == 0) {
            roleRepo.saveAll(Arrays.asList(Role.getInstance("ADMIN"), Role.getInstance("USER")));
        }

    }
}
