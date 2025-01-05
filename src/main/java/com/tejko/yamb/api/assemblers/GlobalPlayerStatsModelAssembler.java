package com.tejko.yamb.api.assemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.dto.responses.GlobalPlayerStatsResponse;
import com.tejko.yamb.domain.models.GlobalPlayerStats;

@Component
public class GlobalPlayerStatsModelAssembler implements RepresentationModelAssembler<GlobalPlayerStats, GlobalPlayerStatsResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public GlobalPlayerStatsModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GlobalPlayerStatsResponse toModel(GlobalPlayerStats globalPlayerStats) {
        GlobalPlayerStatsResponse globalPlayerStatsResponse = modelMapper.map(globalPlayerStats, GlobalPlayerStatsResponse.class);
        return globalPlayerStatsResponse;
    
    }

}
