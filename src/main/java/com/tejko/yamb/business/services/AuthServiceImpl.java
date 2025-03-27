package com.tejko.yamb.business.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tejko.yamb.business.interfaces.AuthService;
// import com.tejko.yamb.domain.models.Clash;
import com.tejko.yamb.domain.models.Game;
import com.tejko.yamb.domain.models.Log;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerPreferences;
import com.tejko.yamb.domain.models.PlayerWithTokens;
import com.tejko.yamb.domain.models.Role;
import com.tejko.yamb.domain.models.Score;
// import com.tejko.yamb.domain.repositories.ClashRepository;
import com.tejko.yamb.domain.repositories.GameRepository;
import com.tejko.yamb.domain.repositories.LogRepository;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.domain.repositories.RoleRepository;
import com.tejko.yamb.domain.repositories.ScoreRepository;
import com.tejko.yamb.security.AuthContext;
import com.tejko.yamb.util.EmailManager;
import com.tejko.yamb.util.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    private final PlayerRepository playerRepo;
    private final RoleRepository roleRepo;
    private final ScoreRepository scoreRepo;
    // private final ClashRepository clashRepo;
    private final LogRepository logRepo;
    private final GameRepository gameRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final EmailManager emailSender;

	@Value("${spring.profiles.active:default}")
	private String activeProfile;

    @Autowired
    public AuthServiceImpl(PlayerRepository playerRepo, ScoreRepository scoreRepo,
                            RoleRepository roleRepo, /*ClashRepository clashRepo, */
                            GameRepository gameRepo, LogRepository logRepo, 
                            JwtUtil jwtUtil, PasswordEncoder encoder, 
                            EmailManager emailSender) {
        this.playerRepo = playerRepo;
        this.scoreRepo = scoreRepo;
        this.roleRepo = roleRepo;
        // this.clashRepo = clashRepo;
        this.gameRepo = gameRepo;
        this.logRepo = logRepo;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
        this.emailSender = emailSender;
    }

    @Override
    public PlayerWithTokens getToken(String email, String username, String password) {
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

        if (AuthContext.isAuthenticated()) {
            mergePlayers(player, List.of(AuthContext.getAuthenticatedPlayer()));
        }

        String newAccessToken = jwtUtil.generateAccessToken(player.getExternalId());
        String newRefreshToken = jwtUtil.generateRefreshToken(player.getExternalId());
        return new PlayerWithTokens(player, newAccessToken, newRefreshToken);    
    }

    @Override
    public PlayerWithTokens migrateToken(String token) {
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Invalid access token");
        }
        UUID playerExternalId = jwtUtil.getPlayerExternalIdFromToken(token);
        Player player = playerRepo.findByExternalId(playerExternalId)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found"));

        String newAccessToken = jwtUtil.generateAccessToken(player.getExternalId());
        String newRefreshToken = jwtUtil.generateRefreshToken(player.getExternalId());
        return new PlayerWithTokens(player, newAccessToken, newRefreshToken);    
    }

    @Override
    public PlayerWithTokens refreshTokens(String refreshToken) {
        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        
        UUID playerExternalId = jwtUtil.getPlayerExternalIdFromToken(refreshToken);
        Player player = playerRepo.findByExternalId(playerExternalId)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found"));
            
        String newAccessToken = jwtUtil.generateAccessToken(player.getExternalId());
        String newRefreshToken = jwtUtil.generateRefreshToken(player.getExternalId());
        return new PlayerWithTokens(player, newAccessToken, newRefreshToken);   
    }

    @Override
    public Player register(String email, String username, String password) {
        validateUsername(username);
        String normalizedEmail = EmailManager.normalizeEmail(email);
        validateEmail(normalizedEmail);
        Player player = Player.getInstance(normalizedEmail, username, encoder.encode(password), getDefaultRoles());
        PlayerPreferences preferences = PlayerPreferences.getInstance(
            player,
            "dark",
            LocaleContextHolder.getLocale().getLanguage()
        );
        player.setPreferences(preferences);
        player = playerRepo.save(player);
        if (AuthContext.isAuthenticated()) {
            mergePlayers(player, List.of(AuthContext.getAuthenticatedPlayer()));
        }

        if (!"dev".equalsIgnoreCase(activeProfile)) emailSender.sendNewUserNotificationEmail("matej@jamb.com.hr", player.getUsername(), "https://jamb.com.hr/players/" + player.getExternalId());
        return player;
    }

    private void validateEmail(String email) {
        if (playerRepo.existsByEmailIgnoreCaseAndEmailVerified(email, true)) {
            throw new IllegalArgumentException("error.email_taken");
        }
    }

    @Override
    public PlayerWithTokens registerGuest(String username) {
        validateUsername(username);
        Player player = Player.getInstance(null, username, null, getDefaultRoles());
        PlayerPreferences preferences = PlayerPreferences.getInstance(
            player,
            "dark",
            LocaleContextHolder.getLocale().getLanguage()
        );
        player.setPreferences(preferences);
        player = playerRepo.save(player);
        if (AuthContext.isAuthenticated()) {
            mergePlayers(player, List.of(AuthContext.getAuthenticatedPlayer()));
        }

        if (!"dev".equalsIgnoreCase(activeProfile)) emailSender.sendNewUserNotificationEmail("matej@jamb.com.hr", player.getUsername(), "https://jamb.com.hr/players/" + player.getExternalId());
        String newAccessToken = jwtUtil.generateAccessToken(player.getExternalId());
        String newRefreshToken = jwtUtil.generateRefreshToken(player.getExternalId());
        return new PlayerWithTokens(player, newAccessToken, newRefreshToken);    
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

    private void mergePlayers(Player parentPlayer, List<Player> players) {
        if (players.isEmpty()) return;

        Long parentId = parentPlayer.getId();
        Set<Long> playerIds = players.stream()
            .map(Player::getId)
            .filter(id -> !id.equals(parentId))
            .collect(Collectors.toSet());
            
        if (playerIds.isEmpty()) return;
         
        UUID parentExternalId = parentPlayer.getExternalId();
        Set<UUID> playerExternalIds = players.stream()
            .map(Player::getExternalId)
            .filter(id -> !id.equals(parentExternalId))
            .collect(Collectors.toSet());


        List<Score> scores = scoreRepo.findAllByPlayerIdInOrderByCreatedAtDesc(playerIds);
        scores.forEach(score -> score.setPlayer(parentPlayer));
        scoreRepo.saveAll(scores);

        List<Game> games = gameRepo.findAllByPlayerIdInOrderByUpdatedAtDesc(playerExternalIds);
        games.forEach(game -> game.setPlayerId(parentPlayer.getExternalId()));
        gameRepo.saveAll(games);

        // List<Clash> clashes = clashRepo.findAllByPlayerIdIn(playerExternalIds);
        // clashes.forEach(clash -> clash.replacePlayer((), parentPlayer.getExternalId()));
        // clashRepo.saveAll(clashes);

        List<Log> logs = logRepo.findAllByPlayerIdInOrderByCreatedAtDesc(playerIds);
        logs.forEach(log -> log.setPlayer(parentPlayer));
        logRepo.saveAll(logs);

        // List<PlayerRelationship> relationships = relationshipRepo.getRelationshipsByPlayerIdIn(playerIds);
        // relationships.forEach(relationship -> {
        //     if (playerIds.contains(relationship.getId().getPlayer().getId())) {
        //         relationship.getId().setPlayer(parentPlayer);
        //     } else if (playerIds.contains(relationship.getId().getRelatedPlayer().getId())) {
        //         relationship.getId().setRelatedPlayer(parentPlayer);
        //     }
        // });
        // relationshipRepo.saveAll(relationships);

        playerRepo.deleteAll(players);
    }

}
