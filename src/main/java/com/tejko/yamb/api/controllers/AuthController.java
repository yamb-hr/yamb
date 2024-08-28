package com.tejko.yamb.api.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.dto.requests.AnonymousPlayerRequest;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.services.interfaces.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisteredPlayer> register(@Valid @RequestBody AuthRequest authRequest) {
        RegisteredPlayer player = authService.register(authRequest);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(player.getId())
            .toUri();
        return ResponseEntity.created(location).body(player);
    }

    @PostMapping("/anonymous")
    public ResponseEntity<AuthResponse> createAnonymousPlayer(@Valid @RequestBody AnonymousPlayerRequest authRequest) {
        AuthResponse response = authService.createAnonymousPlayer(authRequest);
        return ResponseEntity.created(null).body(response);
    }

}