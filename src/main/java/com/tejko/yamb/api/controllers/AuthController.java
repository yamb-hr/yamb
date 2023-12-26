package com.tejko.yamb.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.AuthService;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.models.payload.AuthResult;
import com.tejko.yamb.models.payload.PlayerCredentials;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public AuthResult login(@Valid @RequestBody PlayerCredentials playerCredentials) {
        return authService.login(playerCredentials);
    }

    @PostMapping("/register")
    public Player register(@Valid @RequestBody PlayerCredentials playerCredentials) {
        return authService.register(playerCredentials);
    }

	@PostMapping("/temp-player")
	public AuthResult createTempPlayer(@RequestBody PlayerCredentials playerCredentials) {
		return authService.createTempPlayer(playerCredentials);
	}

}