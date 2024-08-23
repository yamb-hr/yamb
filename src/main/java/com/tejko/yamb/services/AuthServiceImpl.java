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
import com.tejko.yamb.interfaces.services.AuthService;
import com.tejko.yamb.security.JwtUtil;
import com.tejko.yamb.util.PayloadMapper;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PayloadMapper mapper;

    @Override
    public AuthResponse login(AuthRequest playerCredentials) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                playerCredentials.getUsername(), 
                playerCredentials.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Player player = playerRepo.findByUsername(playerCredentials.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_PLAYER_NOT_FOUND));

        return createAuthResponse(player);
    }

    @Override
    public Player register(AuthRequest playerCredentials) {
        String currentUsername = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getName)
            .orElse(SecurityConstants.ANONYMOUS_USER);

        validateUsername(playerCredentials.getUsername());

        Optional<Player> existingPlayer = playerRepo.findByUsername(playerCredentials.getUsername());

        if (existingPlayer.isPresent() && (!existingPlayer.get().isTempUser() || !currentUsername.equals(playerCredentials.getUsername()))) {
            throw new BadCredentialsException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
        }

        Player player = existingPlayer.orElseGet(() -> Player.getInstance(
            playerCredentials.getUsername(),
            encoder.encode(playerCredentials.getPassword()),
            false
        ));

        if (!SecurityConstants.ANONYMOUS_USER.equals(currentUsername)) {
            playerRepo.findByUsername(currentUsername).ifPresent(existing -> {
                player.setPassword(encoder.encode(playerCredentials.getPassword()));
                player.setTempUser(false);
            });
        }

        player.setRoles(assignRoles(playerCredentials.getUsername()));
        return playerRepo.save(player);
    }

    @Override
    public AuthResponse createTempPlayer(AuthRequest tempPlayerCredentials) {
        validateUsername(tempPlayerCredentials.getUsername());

        Player player = Player.getInstance(
            tempPlayerCredentials.getUsername(),
            encoder.encode(UUID.randomUUID().toString()),
            true
        );

        player.setRoles(assignRoles(tempPlayerCredentials.getUsername()));
        player = playerRepo.save(player);

        return createAuthResponse(player);
    }

    public void validateUsername(String username) {
        if (playerRepo.existsByUsername(username)) {
            throw new IllegalArgumentException(MessageConstants.ERROR_USERNAME_ALREADY_TAKEN);
        } else if (username.length() < SecurityConstants.MIN_USERNAME_SIZE || username.length() > SecurityConstants.MAX_USERNAME_SIZE) {
            throw new IllegalArgumentException(MessageConstants.ERROR_INVALID_USERNAME_SIZE);
        }
    }

    public Set<Role> assignRoles(String username) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER")
            .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
        roles.add(userRole);

        if ("matej".equals(username)) {
            Role adminRole = roleRepo.findByLabel("ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.ERROR_ROLE_NOT_FOUND));
            roles.add(adminRole);
        }

        return roles;
    }

    private AuthResponse createAuthResponse(Player player) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.player = mapper.toDTO(player);
        authResponse.token = jwtUtil.generateToken(player.getExternalId());
        return authResponse;
    }
}
