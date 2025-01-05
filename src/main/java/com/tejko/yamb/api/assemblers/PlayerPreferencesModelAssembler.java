package com.tejko.yamb.api.assemblers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.dto.requests.PlayerPreferencesRequest;
import com.tejko.yamb.api.dto.responses.PlayerPreferencesResponse;
import com.tejko.yamb.domain.models.PlayerPreferences;

@Component
public class PlayerPreferencesModelAssembler implements RepresentationModelAssembler<PlayerPreferences, PlayerPreferencesResponse> {

    private final ModelMapper modelMapper;

    @Autowired
    public PlayerPreferencesModelAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PlayerPreferencesResponse toModel(PlayerPreferences playerPreferences) {
        PlayerPreferencesResponse playerPreferencesResponse = modelMapper.map(playerPreferences, PlayerPreferencesResponse.class);
        return playerPreferencesResponse;
    }

    public PlayerPreferences fromModel(PlayerPreferencesRequest playerPreferencesRequest) {
        PlayerPreferences playerPreferences = modelMapper.map(playerPreferencesRequest, PlayerPreferences.class);
        return playerPreferences;
    }
    
}
