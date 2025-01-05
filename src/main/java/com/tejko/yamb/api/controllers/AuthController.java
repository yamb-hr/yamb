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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.AuthModelAssembler;
import com.tejko.yamb.api.assemblers.PlayerDetailModelAssembler;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.requests.EmailRequest;
import com.tejko.yamb.api.dto.requests.PasswordChangeRequest;
import com.tejko.yamb.api.dto.requests.UsernameRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.api.dto.responses.PlayerDetailResponse;
import com.tejko.yamb.business.interfaces.AuthService;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerWithToken;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthModelAssembler authModelAssembler;
    private final PlayerDetailModelAssembler playerDetailModelAssembler;

    @Autowired
    public AuthController(AuthService authService, AuthModelAssembler authModelAssembler, 
                          PlayerDetailModelAssembler playerDetailModelAssembler) {
        this.authService = authService;
        this.authModelAssembler = authModelAssembler;
        this.playerDetailModelAssembler = playerDetailModelAssembler;
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getToken(@Valid @RequestBody AuthRequest authRequest) {
        PlayerWithToken playerWithToken = authService.getToken(authRequest.getEmail(), authRequest.getUsername(), authRequest.getPassword());
        AuthResponse authResponse = authModelAssembler.toModel(playerWithToken);
        authResponse.getPlayer().setEmail(playerWithToken.getPlayer().getEmail());
		authResponse.getPlayer().setEmailVerified(playerWithToken.getPlayer().isEmailVerified());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerDetailResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        
        Player player = authService.register(authRequest.getEmail(), authRequest.getUsername(), authRequest.getPassword());
        PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(player);
        playerDetailResponse.setEmail(player.getEmail());
		playerDetailResponse.setEmailVerified(player.isEmailVerified());
        playerDetailResponse.add(linkTo(methodOn(AuthController.class).getToken(null)).withRel("token"));
                
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(playerDetailResponse.getId())
            .toUri();

        return ResponseEntity.created(location).body(playerDetailResponse);
    }

    @PostMapping("/register-guest")
    public ResponseEntity<AuthResponse> registerGuest(@Valid @RequestBody UsernameRequest usernameRequest) {
        
        AuthResponse authResponse = authModelAssembler.toModel(authService.registerGuest(usernameRequest.getUsername()));
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(authResponse.getPlayer().getId())
            .toUri();

        return ResponseEntity.created(location).body(authResponse);
    }

    @PostMapping("/password-reset-token")
    public ResponseEntity<Void> sendPasswordResetEmail(@Valid @RequestBody EmailRequest emailRequest) {
        authService.sendPasswordResetEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password-reset")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.resetPassword(token, passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email-verification-token")
    public ResponseEntity<Void> sendVerificationEmail(@Valid @RequestBody EmailRequest emailRequest) {
        authService.sendVerificationEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email-verification")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

}
