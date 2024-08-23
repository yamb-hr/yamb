package com.tejko.yamb.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tejko.yamb.api.payload.requests.AuthRequest;
import com.tejko.yamb.api.payload.responses.AuthResponse;
import com.tejko.yamb.api.payload.responses.PlayerResponse;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RoleRepository;
import com.tejko.yamb.security.JwtUtil;
import com.tejko.yamb.services.AuthServiceImpl;
import com.tejko.yamb.util.PayloadMapper;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplUnitTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private PayloadMapper mapper;

    private AuthRequest authRequest;
    private Player player;
    private Role userRole;
    private Role adminRole;
    private Set<Role> roles;
    private String username;
    private String password;

    @BeforeEach
    public void init() {
        username = "username";
        password = "password";
        authRequest = new AuthRequest(username, password);
    
        player = Player.getInstance(username, password, false);
    
        userRole = Role.getInstance("USER");
        adminRole = Role.getInstance("ADMIN");
        
        roles = new HashSet<>();
        roles.add(userRole);
    }

    @Test
    public void testLogin_Success() {
        Authentication auth = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(playerRepo.findByUsername(authRequest.getUsername())).thenReturn(Optional.of(player));
        when(jwtUtil.generateToken(player.getExternalId())).thenReturn("mocked-token");
        when(mapper.toDTO(any(Player.class))).thenReturn(new PlayerResponse());

        AuthResponse response = authService.login(authRequest);

        assertNotNull(response);
        assertEquals("mocked-token", response.token);
    }

    @Test
    public void testLogin_PlayerNotFound() {
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        when(playerRepo.findByUsername(authRequest.getUsername())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(authRequest));
    }

    @Test
    public void testRegister_Success() {
        when(playerRepo.findByUsername(authRequest.getUsername())).thenReturn(Optional.empty());
        when(encoder.encode(any())).thenReturn("encoded-password");
        when(roleRepo.findByLabel("USER")).thenReturn(Optional.of(userRole));
        when(playerRepo.save(any(Player.class))).thenReturn(player);

        Player registeredPlayer = authService.register(authRequest);

        assertNotNull(registeredPlayer);
        verify(playerRepo).save(any(Player.class));
    }

    @Test
    public void testRegister_UsernameAlreadyTaken() {
        when(playerRepo.findByUsername(authRequest.getUsername())).thenReturn(Optional.of(player));

        assertThrows(BadCredentialsException.class, () -> authService.register(authRequest));
    }

    @Test
    public void testCreateTempPlayer_Success() {
        when(playerRepo.existsByUsername(authRequest.getUsername())).thenReturn(false);
        when(encoder.encode(any())).thenReturn("encoded-password");
        when(roleRepo.findByLabel("USER")).thenReturn(Optional.of(userRole));
        when(playerRepo.save(any(Player.class))).thenReturn(player);
        when(jwtUtil.generateToken(any())).thenReturn("mocked-token");
        when(mapper.toDTO(any(Player.class))).thenReturn(new PlayerResponse());

        AuthResponse response = authService.createTempPlayer(authRequest);

        assertNotNull(response);
        assertEquals("mocked-token", response.token);
        verify(playerRepo).save(any(Player.class));
    }

    @Test
    public void testCreateTempPlayer_UsernameAlreadyTaken() {
        when(playerRepo.existsByUsername(authRequest.getUsername())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.createTempPlayer(authRequest));
    }

    @Test
    public void testValidateUsername_UsernameTaken() {
        when(playerRepo.existsByUsername(username)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.validateUsername(username);
        });
    }

    @Test
    public void testValidateUsername_InvalidSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.validateUsername("us");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            authService.validateUsername("username_too_long_for_system");
        });
    }

    @Test
    public void testAssignRoles_UserRoleSuccess() {
        when(roleRepo.findByLabel("USER")).thenReturn(Optional.of(userRole));

        Set<Role> assignedRoles = authService.assignRoles(username);

        assertNotNull(assignedRoles);
        assertEquals(1, assignedRoles.size());
        assertEquals("USER", assignedRoles.iterator().next().getLabel());
    }

    @Test
    public void testAssignRoles_AdminRoleSuccess() {
        when(roleRepo.findByLabel("USER")).thenReturn(Optional.of(userRole));
        when(roleRepo.findByLabel("ADMIN")).thenReturn(Optional.of(adminRole));

        Set<Role> assignedRoles = authService.assignRoles("matej");

        assertNotNull(assignedRoles);
        assertEquals(2, assignedRoles.size());
    }

    @Test
    public void testAssignRoles_RoleNotFound() {
        when(roleRepo.findByLabel("USER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            authService.assignRoles(username);
        });
    }
}
