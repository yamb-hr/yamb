package com.tejko.yamb.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.yamb.api.services.AuthService;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.models.payload.AuthRequest;
import com.tejko.yamb.models.payload.LoginResponse;
import com.tejko.yamb.models.payload.TempPlayerRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.login(authRequest), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Player> register(@Valid @RequestBody AuthRequest authRequest) {
        return new ResponseEntity<>(authService.register(authRequest), HttpStatus.OK);
    }

	@PostMapping("/temp-player")
	public ResponseEntity<LoginResponse> createTempPlayer(@Valid @RequestBody TempPlayerRequest tempPlayerRequest) {
		return new ResponseEntity<>(authService.createTempPlayer(tempPlayerRequest), HttpStatus.OK);
	}

}