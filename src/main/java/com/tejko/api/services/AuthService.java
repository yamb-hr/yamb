package com.tejko.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.constants.GameConstants;
import com.tejko.models.Player;
import com.tejko.models.payload.AuthRequest;
import com.tejko.models.payload.LoginResponse;
import com.tejko.repositories.PlayerRepository;
import com.tejko.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder encoder;

    public LoginResponse login(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Player player = playerRepo.findByUsername(authRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException(GameConstants.ERROR_PLAYER_NOT_FOUND));
            String token = jwtUtil.generateToken(player);
            LoginResponse response = new LoginResponse(token);
            return response;
        } catch (InternalAuthenticationServiceException exception) {
            throw new BadCredentialsException(GameConstants.ERROR_USERNAME_OR_PASSWORD_INCORRECT);
        }
    }

    public Player register(AuthRequest authRequest) {
		if (playerRepo.existsByUsername(authRequest.getUsername())) {
			throw new BadCredentialsException(GameConstants.ERROR_USER_ALREADY_EXISTS);
		}
        Player user = Player.getInstance(
            authRequest.getUsername(), 
            encoder.encode(authRequest.getPassword())
        );
        return playerRepo.save(user);
    }

}
