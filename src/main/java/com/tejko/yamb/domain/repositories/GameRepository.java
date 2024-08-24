package com.tejko.yamb.domain.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tejko.yamb.domain.models.Game;

public interface GameRepository extends MongoRepository<Game, String> {
    
}