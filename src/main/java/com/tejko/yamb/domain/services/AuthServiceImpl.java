package com.tejko.yamb.domain.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.api.dto.requests.AnonymousPlayerRequest;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.responses.AuthResponse;
import com.tejko.yamb.domain.models.AnonymousPlayer;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.RegisteredPlayer;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RoleRepository;
import com.tejko.yamb.domain.services.interfaces.AuthService;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.CustomObjectMapper;
import com.tejko.yamb.util.I18nUtil;
import com.tejko.yamb.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final PlayerRepository playerRepo;
    private final RoleRepository roleRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final CustomObjectMapper mapper;
    private final I18nUtil i18nUtil;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authManager, PlayerRepository playerRepo, RoleRepository roleRepo, 
            JwtUtil jwtUtil, PasswordEncoder encoder, CustomObjectMapper mapper, I18nUtil i18nUtil) {
        this.authManager = authManager;
        this.playerRepo = playerRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.mapper = mapper;
        this.i18nUtil = i18nUtil;
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        RegisteredPlayer player = authenticate(authRequest.getUsername(), authRequest.getPassword());
        return new AuthResponse(mapper.mapToResponse(player), jwtUtil.generateToken(player.getId()));
    }

    @Override
    public RegisteredPlayer register(AuthRequest authRequest) {
        Optional<Player> authenticatedPlayer = AuthContext.getAuthenticatedPlayer();

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
    
    @Override
    public AuthResponse createAnonymousPlayer(AnonymousPlayerRequest authRequest) {
        validateUsername(authRequest.getUsername());
        AnonymousPlayer player = AnonymousPlayer.getInstance(authRequest.getUsername(), getDefaultRoles());

        player = playerRepo.save(player);
        return new AuthResponse(mapper.mapToResponse(player), jwtUtil.generateToken(player.getId()));
    }

    private void validateUsername(String username) {
        if (playerRepo.existsByUsername(username)) {
            throw new IllegalArgumentException(i18nUtil.getMessage("error.username_taken"));
        }
    }

    private RegisteredPlayer authenticate(String username, String password) {
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return (RegisteredPlayer) authentication.getPrincipal();
    }

    private Set<Role> getDefaultRoles() {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER")
            .orElseThrow(() -> new ResourceNotFoundException(i18nUtil.getMessage("error.not_found.role")));
        roles.add(userRole);
        return roles;
    }

}
