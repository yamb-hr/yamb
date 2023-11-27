package com.tejko.yamb.api.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.constants.MessageConstants;
import com.tejko.yamb.constants.SecurityConstants;
import com.tejko.yamb.models.Player;
import com.tejko.yamb.models.payload.AuthRequest;
import com.tejko.yamb.models.payload.LoginResponse;
import com.tejko.yamb.models.payload.TempPlayerRequest;
import com.tejko.yamb.repositories.PlayerRepository;
import com.tejko.yamb.security.JwtUtil;

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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Player player = playerRepo.findByUsername(authRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return new LoginResponse(player.getId(), player.getUsername(), jwtUtil.generateToken(player.getUsername()));
    }

    public Player register(AuthRequest authRequest) {
		if (playerRepo.existsByUsername(authRequest.getUsername())) {
			throw new BadCredentialsException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
		}
        Player player = Player.getInstance(authRequest.getUsername(), encoder.encode(authRequest.getPassword()));
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(SecurityConstants.ANONYMOUS_USER)) { // in case a user is already a temp player
            Optional<Player> optionalPlayer = playerRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (optionalPlayer.isPresent()) {
                player = optionalPlayer.get();
                player.setUsername(authRequest.getUsername());
                player.setPassword(encoder.encode(authRequest.getPassword()));
            }
        }
        return playerRepo.save(player);
    }

    public LoginResponse createTempPlayer(TempPlayerRequest tempPlayerRequest) {
        if (playerRepo.existsByUsername(tempPlayerRequest.getUsername())) {
            throw new IllegalArgumentException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
        } 
        Player player = Player.getInstance(tempPlayerRequest.getUsername(),  encoder.encode(UUID.randomUUID().toString()));
        // Cookie cookie = new Cookie(SecurityConstants.COOKIE_TOKEN, jwtUtil.generateToken(player.getUsername()));
        // cookie.setMaxAge(365 * 24 * 60 * 60); // expires in 1 year
        // cookie.setSecure(true);
        // cookie.setHttpOnly(true);
        // cookie.setPath("/"); // global cookie accessible every where
        // response.addCookie(cookie);
        player = playerRepo.save(player);
        return new LoginResponse(player.getId(), player.getUsername(), jwtUtil.generateToken(player.getUsername()));
    }

}
