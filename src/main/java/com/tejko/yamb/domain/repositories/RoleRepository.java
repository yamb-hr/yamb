package com.tejko.yamb.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.domain.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByLabel(String label);

}
