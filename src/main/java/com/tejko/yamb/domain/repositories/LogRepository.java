package com.tejko.yamb.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tejko.yamb.interfaces.BaseRepository;
import com.tejko.yamb.domain.models.Log;

public interface LogRepository extends JpaRepository<Log, Long>, BaseRepository<Log> {

}
