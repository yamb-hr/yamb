package com.tejko.yamb.services;

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

import com.tejko.yamb.api.payload.requests.AuthRequest;
import com.tejko.yamb.api.payload.responses.AuthResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.constants.SecurityConstants;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RoleRepository;
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

    public AuthResponse login(AuthRequest playerCredentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                playerCredentials.getUsername(), playerCredentials.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Player player = playerRepo.findByUsername(playerCredentials.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));
        return new AuthResponse(player.getExternalId(), player.getUsername(), jwtUtil.generateToken(player.getUsername()),
                player.isTempUser(), player.getRoles());
    }

    public Player register(AuthRequest playerCredentials) {
        String usernameFromAuth = SecurityContextHolder.getContext().getAuthentication().getName();
        if (playerRepo.existsByUsername(playerCredentials.getUsername())) {
            Player player = playerRepo.findByUsername(playerCredentials.getUsername()).get();
            if (!player.isTempUser() || !playerCredentials.getUsername().equals(usernameFromAuth)) {
                throw new BadCredentialsException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
            }
        } else if (playerCredentials.getUsername().length() < SecurityConstants.MIN_USERNAME_SIZE
                || playerCredentials.getUsername().length() > SecurityConstants.MAX_USERNAME_SIZE) {
            throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_USERNAME_SIZE);
        }
        Player player = Player.getInstance(playerCredentials.getUsername(),
                encoder.encode(playerCredentials.getPassword()), false);
        if (usernameFromAuth != SecurityConstants.ANONYMOUS_USER) {
            Optional<Player> optionalPlayer = playerRepo.findByUsername(usernameFromAuth);
            if (optionalPlayer.isPresent()) {
                player = optionalPlayer.get();
                player.setUsername(playerCredentials.getUsername());
                player.setPassword(encoder.encode(playerCredentials.getPassword()));
                player.setTempUser(false);
            }
        }
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER")
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
        if (playerCredentials.getUsername().equals("matej")) {
            Role adminRole = roleRepo.findByLabel("ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(adminRole);
        }
        roles.add(userRole);
        player.setRoles(roles);
        return playerRepo.save(player);
    }

    public AuthResponse createTempPlayer(AuthRequest tempPlayerCredentials) {
        if (playerRepo.existsByUsername(tempPlayerCredentials.getUsername())) {
            throw new IllegalArgumentException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
        } else if (tempPlayerCredentials.getUsername().length() < SecurityConstants.MIN_USERNAME_SIZE
                || tempPlayerCredentials.getUsername().length() > SecurityConstants.MAX_USERNAME_SIZE) {
            throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_USERNAME_SIZE);
        }
        Player player = Player.getInstance(tempPlayerCredentials.getUsername(),
                encoder.encode(UUID.randomUUID().toString()), true);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER")
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
        if (tempPlayerCredentials.getUsername().equals("matej")) {
            Role adminRole = roleRepo.findByLabel("ADMIN")
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(adminRole);
        }
        roles.add(userRole);
        player.setRoles(roles);

        player = playerRepo.save(player);
        return new AuthResponse(player.getExternalId(), player.getUsername(), jwtUtil.generateToken(player.getUsername()),
                player.isTempUser(), player.getRoles());
    }

}
