package com.tejko.yamb.interfaces;

import java.util.List;
import java.util.UUID;

import com.tejko.yamb.domain.models.BaseEntity;

public interface BaseService<T extends BaseEntity> {

        T getByExternalId(UUID externalId);

        List<T> getAll(Integer page, Integer size, String sort, String direction);
                
        void deleteByExternalId(UUID externalId);
    
}