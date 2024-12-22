package com.tejko.yamb.api.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tejko.yamb.api.controllers.AuthController;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.domain.models.PlayerWithToken;

@Component
public class AuthModelAssembler implements RepresentationModelAssembler<PlayerWithToken, AuthResponse> {

    private final ModelMapper modelMapper;
    private final PlayerModelAssembler playerModelAssembler;

    @Autowired
    public AuthModelAssembler(ModelMapper modelMapper, PlayerModelAssembler playerModelAssembler) {
        this.modelMapper = modelMapper;
        this.playerModelAssembler = playerModelAssembler;
    }

    @Override
    public AuthResponse toModel(PlayerWithToken playerWithToken) {

        AuthResponse authResponse = modelMapper.map(playerWithToken, AuthResponse.class);
        authResponse.add(linkTo(methodOn(AuthController.class).sendPasswordResetEmail(null)).withRel("password-reset-token"));
        authResponse.add(linkTo(methodOn(AuthController.class).resetPassword(null, null)).withRel("password-token"));
        authResponse.add(linkTo(methodOn(AuthController.class).verifyEmail(null)).withRel("verify-email"));
        authResponse.setPlayer(playerModelAssembler.toModel(playerWithToken.getPlayer()));
        
        return authResponse;
    }

}
