package com.tejko.yamb.interfaces;

import java.util.List;

public interface RestService<T> {

        T getById(Long id);
    
        List<T> getAll(Integer page, Integer size, String sort, String direction);
                
        void deleteById(Long id);
    
}