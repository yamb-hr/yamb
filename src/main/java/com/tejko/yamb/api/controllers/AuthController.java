package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.AuthModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerModelAssembler;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.requests.PasswordChangeRequest;
import com.tejko.yamb.api.dto.requests.UsernameRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.api.dto.responses.PlayerResponse;
import com.tejko.yamb.business.interfaces.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthModelAssembler authModelAssembler;
    private final PlayerModelAssembler playerModelAssembler;

    @Autowired
    public AuthController(AuthService authService, AuthModelAssembler authModelAssembler, PlayerModelAssembler playerModelAssembler) {
        this.authService = authService;
        this.authModelAssembler = authModelAssembler;
        this.playerModelAssembler = playerModelAssembler;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authModelAssembler.toModel(authService.getToken(authRequest.getUsername(), authRequest.getPassword()));
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        
        PlayerResponse playerResponse = playerModelAssembler.toModel(authService.register(authRequest.getUsername(), authRequest.getPassword()));
        playerResponse.add(linkTo(methodOn(AuthController.class).getToken(null)).withRel("token"));
                
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(playerResponse.getId())
            .toUri();

        return ResponseEntity.created(location).body(playerResponse);
    }

    @PostMapping("/guest")
    public ResponseEntity<AuthResponse> registerGuest(@Valid @RequestBody UsernameRequest anonymousPlayerRequest) {
        
        AuthResponse authResponse = authModelAssembler.toModel(authService.registerGuest(anonymousPlayerRequest.getUsername()));
        authResponse.add(linkTo(methodOn(AuthController.class).register(null)).withRel("register"));

        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(authResponse.getPlayer().getId())
            .toUri();

        return ResponseEntity.created(location).body(authResponse);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.changePassword(passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
        
        return ResponseEntity.noContent()
            .location(linkTo(methodOn(AuthController.class).getToken(null)).toUri())
            .build();
    }
}
