package com.tejko.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tejko.api.services.AuthService;
import com.tejko.exceptions.UsernameTakenException;
import com.tejko.models.Player;
import com.tejko.models.payload.AuthRequest;
import com.tejko.models.payload.LoginResponse;

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
    public ResponseEntity<Player> register(@Valid @RequestBody AuthRequest authRequest) throws UsernameTakenException {
        return new ResponseEntity<>(authService.register(authRequest), HttpStatus.OK);
    }

}