package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.tejko.yamb.business.interfaces.EmailService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final AuthModelAssembler authModelAssembler;
    private final PlayerModelAssembler playerModelAssembler;

    @Autowired
    public AuthController(AuthService authService, EmailService emailService, AuthModelAssembler authModelAssembler, PlayerModelAssembler playerModelAssembler) {
        this.authService = authService;
        this.emailService = emailService;
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

            emailService.sendSimpleMessage("matej@jamb.com.hr", "New User: " + authRequest.getUsername(), "User " + authRequest.getUsername() + " registered on " + LocalDateTime.now());

        return ResponseEntity.created(location).body(playerResponse);
    }

    @PostMapping("/register-guest")
    public ResponseEntity<AuthResponse> registerGuest(@Valid @RequestBody UsernameRequest usernameRequest) {
        
        AuthResponse authResponse = authModelAssembler.toModel(authService.registerGuest(usernameRequest.getUsername()));
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(authResponse.getPlayer().getId())
            .toUri();

            emailService.sendSimpleMessage("matej@jamb.com.hr", "New User: " + usernameRequest.getUsername(), "User " + usernameRequest.getUsername() + " registered on " + LocalDateTime.now());

        return ResponseEntity.created(location).body(authResponse);
    }

    @PutMapping("/password-reset")
	@PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> resetPassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.changePassword(passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
        
        return ResponseEntity.noContent()
            .location(linkTo(methodOn(AuthController.class).getToken(null)).toUri())
            .build();
    }
}
