package com.tejko.yamb.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public AuthResponse login(@Valid @RequestBody AuthRequest playerCredentials) {
        return authService.login(playerCredentials);
    }

    @PostMapping("/register")
    public RegisteredPlayer register(@Valid @RequestBody AuthRequest playerCredentials) {
        return authService.register(playerCredentials);
    }

	@PostMapping("/anon")
	public AuthResponse createAnonymousPlayer(@RequestBody AuthRequest playerCredentials) {
		return authService.createAnonymousPlayer(playerCredentials);
	}

}