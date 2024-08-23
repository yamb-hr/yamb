package com.tejko.yamb.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.payload.requests.AuthRequest;
import com.tejko.yamb.api.payload.responses.AuthResponse;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.interfaces.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest playerCredentials) {
        return authService.login(playerCredentials);
    }

    @PostMapping("/register")
    public Player register(@Valid @RequestBody AuthRequest playerCredentials) {
        return authService.register(playerCredentials);
    }

	@PostMapping("/temp-player")
	public AuthResponse createTempPlayer(@RequestBody AuthRequest playerCredentials) {
		return authService.createTempPlayer(playerCredentials);
	}

}