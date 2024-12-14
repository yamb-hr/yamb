package com.tejko.yamb.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.domain.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByExternalId(UUID externalId);
    
}
