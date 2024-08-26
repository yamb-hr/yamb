package com.tejko.yamb.domain.services;

import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.domain.constants.MessageConstants;
import com.tejko.yamb.domain.constants.SecurityConstants;
import com.tejko.yamb.domain.models.AnonymousPlayer;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RoleRepository;
import com.tejko.yamb.domain.services.interfaces.AuthService;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthContext authContext;
    private final PlayerRepository playerRepo;
    private final RoleRepository roleRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final CustomObjectMapper mapper;

    @Autowired
    public AuthServiceImpl(AuthContext authContext, PlayerRepository playerRepo, RoleRepository roleRepo, JwtUtil jwtUtil, PasswordEncoder encoder, CustomObjectMapper mapper) {
        this.authContext = authContext;
        this.playerRepo = playerRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.mapper = mapper;
    }

    @Override
    public AuthResponse createAnonymousPlayer(AuthRequest tempPlayerCredentials) {
        validateUsername(tempPlayerCredentials.getUsername());
        AnonymousPlayer player = AnonymousPlayer.getInstance(tempPlayerCredentials.getUsername(), getDefaultRoles());

        player = playerRepo.save(player);
        return new AuthResponse(mapper.mapToResponse(player), jwtUtil.generateToken(player.getId()));
    }

    @Override
    public AuthResponse login(AuthRequest playerCredentials) {
        RegisteredPlayer player = authContext.authenticate(playerCredentials.getUsername(), playerCredentials.getPassword());
        return new AuthResponse(mapper.mapToResponse(player), jwtUtil.generateToken(player.getId()));
    }

    @Override
    public RegisteredPlayer register(AuthRequest authRequest) {
        Optional<Player> authenticatedPlayer = authContext.getAuthenticatedPlayer();

        RegisteredPlayer player;

        if (authenticatedPlayer.isPresent() && Hibernate.getClass(authenticatedPlayer.get()).equals(AnonymousPlayer.class)) {
            if (!authenticatedPlayer.get().getUsername().equals(authRequest.getUsername())) {
                validateUsername(authRequest.getUsername());
            }
            player = (RegisteredPlayer) authenticatedPlayer.get();
            player.setUsername(authRequest.getUsername());
            player.setPassword(encoder.encode(authRequest.getPassword()));
        } else {
            validateUsername(authRequest.getUsername());
            player = RegisteredPlayer.getInstance(authRequest.getUsername(), encoder.encode(authRequest.getPassword()), getDefaultRoles());
        }

        return playerRepo.save(player);
    }

    private void validateUsername(String username) {
        if (playerRepo.existsByUsername(username)) {
            throw new IllegalArgumentException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
        } else if (username.length() < SecurityConstants.MIN_USERNAME_SIZE || username.length() > SecurityConstants.MAX_USERNAME_SIZE) {
            throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_USERNAME_SIZE);
        }
    }

    private Set<Role> getDefaultRoles() {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER")
            .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
        roles.add(userRole);
        return roles;
    }

}
