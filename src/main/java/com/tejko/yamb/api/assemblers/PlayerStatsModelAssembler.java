package com.tejko.yamb.api.assemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.dto.responses.PlayerStatsResponse;
import com.tejko.yamb.domain.models.PlayerStats;

@Component
public class PlayerStatsModelAssembler implements RepresentationModelAssembler<PlayerStats, PlayerStatsResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public PlayerStatsModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PlayerStatsResponse toModel(PlayerStats PlayerStats) {
        PlayerStatsResponse playerStatsResponse = modelMapper.map(PlayerStats, PlayerStatsResponse.class);
        return playerStatsResponse;
    }

}
