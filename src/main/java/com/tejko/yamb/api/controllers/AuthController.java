package com.tejko.yamb.api.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.dto.requests.UsernameRequest;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.requests.PasswordChangeRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.business.interfaces.AuthService;
import com.tejko.yamb.domain.models.entities.RegisteredPlayer;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = modelMapper.map(authService.login(authRequest.getUsername(), authRequest.getPassword()), AuthResponse.class);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisteredPlayer> register(@Valid @RequestBody AuthRequest authRequest) {
        RegisteredPlayer registeredPlayer = modelMapper.map(authService.register(authRequest.getUsername(), authRequest.getPassword()), RegisteredPlayer.class);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(registeredPlayer.getId())
            .toUri();
        return ResponseEntity.created(location).body(registeredPlayer);
    }

    @PostMapping("/anonymous")
    public ResponseEntity<AuthResponse> createAnonymousPlayer(@Valid @RequestBody UsernameRequest anonymousPlayerRequest) {
        AuthResponse authResponse = modelMapper.map(authService.createAnonymousPlayer(anonymousPlayerRequest.getUsername()), AuthResponse.class);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(authResponse.getPlayer().getId())
            .toUri();
        return ResponseEntity.created(location).body(authResponse);
    }

	@PutMapping("/password")
	public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
		authService.changePassword(passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
		return ResponseEntity.noContent().build();
	}

}