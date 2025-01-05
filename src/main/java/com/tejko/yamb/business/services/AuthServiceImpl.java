package com.tejko.yamb.business.services;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.AuthService;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerWithToken;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RoleRepository;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.EmailManager;
import com.tejko.yamb.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final PlayerRepository playerRepo;
    private final RoleRepository roleRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final EmailManager emailSender;

    @Autowired
    public AuthServiceImpl(PlayerRepository playerRepo, RoleRepository roleRepo, 
                            JwtUtil jwtUtil, PasswordEncoder encoder, EmailManager emailSender) {
        this.playerRepo = playerRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.emailSender = emailSender;
    }

    @Override
    public PlayerWithToken getToken(String email, String username, String password) {
        Player player;
        String normalizedEmail = EmailManager.normalizeEmail(email);
        if (email != null && !email.isEmpty()) {
            player = playerRepo.findByEmail(normalizedEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
        } else if (username != null && !username.isEmpty()) {
            player = playerRepo.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("Username not found"));
        } else {
            throw new IllegalArgumentException("Either email or username must be provided");
        }

        if (!encoder.matches(password, player.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return new PlayerWithToken(player, jwtUtil.generateToken(player.getExternalId()));
    }

    @Override
    public Player register(String email, String username, String password) {
        validateUsername(username);
        String normalizedEmail = EmailManager.normalizeEmail(email);
        validateEmail(normalizedEmail);
        Player player = Player.getInstance(normalizedEmail, username, encoder.encode(password), getDefaultRoles());
        player = playerRepo.save(player);
        emailSender.sendNewUserNotificationEmail("matej@jamb.com.hr", player.getUsername(), "https://jamb.com.hr/players/" + player.getExternalId());
        return player;
    }

    private void validateEmail(String email) {
        if (playerRepo.existsByEmailIgnoreCaseAndEmailVerified(email, true)) {
            throw new IllegalArgumentException("error.email_taken");
        }
    }

    @Override
    public PlayerWithToken registerGuest(String username) {
        validateUsername(username);
        Player player = Player.getInstance(null, username, null, getDefaultRoles());
        player = playerRepo.save(player);
        emailSender.sendNewUserNotificationEmail("matej@jamb.com.hr", player.getUsername(), "https://jamb.com.hr/players/" + player.getExternalId());
        return new PlayerWithToken(player, jwtUtil.generateToken(player.getExternalId()));
    }

    @Override
    public void resetPassword(String token, String oldPassword, String newPassword) {
        Player player = AuthContext.getAuthenticatedPlayer();

        if ((token == null || token != null && !token.equals(player.getPasswordResetToken())) && player.getPassword() != null && (oldPassword == null || !encoder.matches(oldPassword, player.getPassword()))) {
            throw new IllegalArgumentException("Invalid password reset request");
        }
        player.setPassword(encoder.encode(newPassword));
        playerRepo.save(player);
    }

    @Override
    public void verifyEmail(String token) {
        Player player = playerRepo.findByEmailVerificationToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

        player.setEmailVerified(true);
        player.setEmailVerificationToken(null);
        playerRepo.save(player);
    }

    @Override
    public void sendPasswordResetEmail(String email) {
        String normalizedEmail = EmailManager.normalizeEmail(email);
        Player player = playerRepo.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        if (!player.isEmailVerified()) {
            throw new IllegalStateException("Email is not yet verified.");
        }

        String token = UUID.randomUUID().toString();
        player.setPasswordResetToken(token);
        playerRepo.save(player);

        String passwordResetLink = "https://jamb.com.hr/password-reset?token=" + token;
        emailSender.sendPasswordResetEmail(player.getEmail(), player.getUsername(), passwordResetLink);
    }

    @Override
    public void sendVerificationEmail(String email) {
        String normalizedEmail = EmailManager.normalizeEmail(email);
        Player player = playerRepo.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        if (player.isEmailVerified()) {
            throw new IllegalStateException("Email is already verified.");
        }

        String token = UUID.randomUUID().toString();
        player.setEmailVerificationToken(token);
        playerRepo.save(player);

        String emailVerificationLink = "https://jamb.com.hr/verify-email?token=" + token;
        emailSender.sendVerificationEmail(player.getEmail(), player.getUsername(), emailVerificationLink);
    }

    private void validateUsername(String username) {
        if (playerRepo.existsByUsernameIgnoreCase(username)) {
            throw new IllegalStateException("error.username_taken");
        }
    }

    private Set<Role> getDefaultRoles() {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByLabel("USER")
            .orElseThrow(() -> new ResourceNotFoundException("error.not_found.role"));
        roles.add(userRole);
        return roles;
    }

}
