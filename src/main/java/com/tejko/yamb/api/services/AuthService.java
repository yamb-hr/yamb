package com.tejko.yamb.api.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
import com.tejko.yamb.models.Role;
import com.tejko.yamb.models.payload.AuthRequest;
import com.tejko.yamb.models.payload.LoginResponse;
import com.tejko.yamb.models.payload.TempPlayerRequest;
import com.tejko.yamb.repositories.PlayerRepository;
import com.tejko.yamb.repositories.RoleRepository;
import com.tejko.yamb.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PlayerRepository playerRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PasswordEncoder encoder;

    public LoginResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Player player = playerRepo.findByUsername(authRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return new LoginResponse(player.getId(), player.getUsername(), jwtUtil.generateToken(player.getUsername()), player.isTempUser(), player.getRoles());
    }

    public Player register(AuthRequest authRequest) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
		if (playerRepo.existsByUsername(authRequest.getUsername())) {
            Player player = playerRepo.findByUsername(authRequest.getUsername()).get();
            if (!player.isTempUser() || !authRequest.getUsername().equals(usernameFromAuth)) {
                throw new BadCredentialsException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
            }
		} else if (authRequest.getUsername().length() < SecurityConstants.MIN_USERNAME_SIZE || authRequest.getUsername().length() > SecurityConstants.MAX_USERNAME_SIZE) {
            throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_USERNAME_SIZE);
        }
        Player player = Player.getInstance(authRequest.getUsername(), encoder.encode(authRequest.getPassword()), false);
        if (usernameFromAuth != SecurityConstants.ANONYMOUS_USER) {
            Optional<Player> optionalPlayer = playerRepo.findByUsername(usernameFromAuth);
            if (optionalPlayer.isPresent()) {
                player = optionalPlayer.get();
                player.setUsername(authRequest.getUsername());
                player.setPassword(encoder.encode(authRequest.getPassword()));
                player.setTempUser(false);
            }
        }
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER").orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
        if (authRequest.getUsername().equals("matej")) {
            Role adminRole = roleRepo.findByLabel("ADMIN").orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(adminRole);
        }
        roles.add(userRole);
        player.setRoles(roles);
        return playerRepo.save(player);
    }

    public LoginResponse createTempPlayer(TempPlayerRequest tempPlayerRequest) {
        if (playerRepo.existsByUsername(tempPlayerRequest.getUsername())) {
            throw new IllegalArgumentException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
        } else if (tempPlayerRequest.getUsername().length() < SecurityConstants.MIN_USERNAME_SIZE || tempPlayerRequest.getUsername().length() > SecurityConstants.MAX_USERNAME_SIZE) {
            throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_USERNAME_SIZE);
        }   
        Player player = Player.getInstance(tempPlayerRequest.getUsername(),  encoder.encode(UUID.randomUUID().toString()), true);
        
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER").orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
        if (tempPlayerRequest.getUsername().equals("matej")) {
            Role adminRole = roleRepo.findByLabel("ADMIN").orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(adminRole);
        }
        roles.add(userRole);
        player.setRoles(roles);

        player = playerRepo.save(player);
        return new LoginResponse(player.getId(), player.getUsername(), jwtUtil.generateToken(player.getUsername()), player.isTempUser(), player.getRoles());
    }

}
