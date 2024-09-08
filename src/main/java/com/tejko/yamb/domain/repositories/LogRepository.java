package com.tejko.yamb.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.domain.models.entities.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByOrderByCreatedAtDesc();
    
}
