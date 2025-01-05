package com.tejko.yamb.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.Mapping;
import org.modelmapper.spi.PropertyMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortFieldTranslator {

    private final ModelMapper modelMapper;

    @Autowired
    public SortFieldTranslator(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Pageable translateSortField(Pageable pageable, Class<?> entityClass, Class<?> dtoClass) {
        List<Sort.Order> mappedOrders = pageable.getSort().stream()
            .map(order -> {
                String translatedProperty = translateProperty(order.getProperty(), entityClass, dtoClass);
                return new Sort.Order(order.getDirection(), translatedProperty);
            })
            .collect(Collectors.toList());

        Sort translatedSort = Sort.by(mappedOrders);
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), translatedSort);
    }

    private String translateProperty(String responseProperty, Class<?> entityClass, Class<?> dtoClass) {
        List<Mapping> mappings = modelMapper.getTypeMap(entityClass, dtoClass).getMappings();

        return mappings.stream()
            .filter(mapping -> mapping instanceof PropertyMapping)
            .map(mapping -> (PropertyMapping) mapping)
            .filter(mapping -> mapping.getLastDestinationProperty().getName().equals(responseProperty))
            .map(mapping -> mapping.getLastSourceProperty().getName())
            .findFirst()
            .orElse(responseProperty);
    }
}
