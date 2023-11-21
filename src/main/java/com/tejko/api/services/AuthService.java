package com.tejko.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.components.JwtComponent;
import com.tejko.exceptions.UsernameTakenException;
import com.tejko.models.Player;
import com.tejko.models.payload.AuthRequest;
import com.tejko.models.payload.LoginResponse;
import com.tejko.repositories.PlayerRepository;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    JwtComponent jwtComponent;

    @Autowired
    PasswordEncoder encoder;

    public LoginResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtComponent.generateJwt(authentication);
        LoginResponse response = new LoginResponse(jwt);
        return response;
    }

    public Player register(AuthRequest authRequest) throws UsernameTakenException {
        Player user = new Player(
            authRequest.username, 
            encoder.encode(authRequest.password)
        );
        return playerRepository.save(user);
    }

}
