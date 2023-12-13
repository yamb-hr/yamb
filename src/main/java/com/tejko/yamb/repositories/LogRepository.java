package com.tejko.yamb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.models.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

}
