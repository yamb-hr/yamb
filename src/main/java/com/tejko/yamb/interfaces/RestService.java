package com.tejko.yamb.interfaces;

import java.util.List;

import com.tejko.yamb.models.DatabaseEntity;

public interface RestService<T extends DatabaseEntity> {

        T getById(Long id);
    
        List<T> getAll(Integer page, Integer size, String sort, String direction);
                
        void deleteById(Long id);
    
}