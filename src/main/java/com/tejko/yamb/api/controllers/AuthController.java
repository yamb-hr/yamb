package com.tejko.yamb.api.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tejko.yamb.api.assemblers.PlayerDetailModelAssembler;
import com.tejko.yamb.api.dto.requests.AuthRequest;
import com.tejko.yamb.api.dto.requests.EmailRequest;
import com.tejko.yamb.api.dto.requests.PasswordChangeRequest;
import com.tejko.yamb.api.dto.requests.UsernameRequest;
import com.tejko.yamb.api.dto.responses.PlayerDetailResponse;
import com.tejko.yamb.business.interfaces.AuthService;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.models.PlayerWithTokens;
import com.tejko.yamb.util.TokenExtractor;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PlayerDetailModelAssembler playerDetailModelAssembler;

    private static final String COOKIE_ACCESS_TOKEN_NAME = "ACCESS_TOKEN";
    private static final String COOKIE_REFRESH_TOKEN_NAME = "REFRESH_TOKEN";

    private static final String SAME_SITE_LAX = "Lax";

    @Autowired
    public AuthController(AuthService authService, PlayerDetailModelAssembler playerDetailModelAssembler) {
        this.authService = authService;
        this.playerDetailModelAssembler = playerDetailModelAssembler;
    }

    @PostMapping("/token")
    public ResponseEntity<PlayerDetailResponse> getToken(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response) {
        PlayerWithTokens playerWithTokens = authService.getToken(authRequest.getEmail(), authRequest.getUsername(), authRequest.getPassword());
        for (ResponseCookie cookie : generateCookies(playerWithTokens)) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(playerWithTokens.getPlayer());
        return ResponseEntity.ok(playerDetailResponse);
    }

    @PostMapping("/migrate")
    public ResponseEntity<PlayerDetailResponse> migrateToken(HttpServletRequest request, HttpServletResponse response) {
        String token = TokenExtractor.extractToken(request);
        PlayerWithTokens playerWithTokens = authService.migrateToken(token);
        for (ResponseCookie cookie : generateCookies(playerWithTokens)) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(playerWithTokens.getPlayer());
        return ResponseEntity.ok(playerDetailResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<PlayerDetailResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (COOKIE_REFRESH_TOKEN_NAME.equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            for (ResponseCookie cookie : generateClearCookies()) {
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PlayerWithTokens playerWithTokens = authService.refreshTokens(refreshToken);
        for (ResponseCookie cookie : generateCookies(playerWithTokens)) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(playerWithTokens.getPlayer());
        return ResponseEntity.ok(playerDetailResponse);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        for (ResponseCookie cookie : generateClearCookies()) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.noContent()
            .location(linkTo(methodOn(AuthController.class).getToken(null, null)).toUri())
            .build();
    }

    @PostMapping("/register")
    public ResponseEntity<PlayerDetailResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        Player player = authService.register(authRequest.getEmail(), authRequest.getUsername(), authRequest.getPassword());
        PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(player);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(playerDetailResponse.getId())
            .toUri();
        return ResponseEntity.created(location).body(playerDetailResponse);
    }

    @PostMapping("/register-guest")
    public ResponseEntity<PlayerDetailResponse> registerGuest(@Valid @RequestBody UsernameRequest usernameRequest, HttpServletResponse response) {
        PlayerWithTokens playerWithTokens = authService.registerGuest(usernameRequest.getUsername());
        for (ResponseCookie cookie : generateCookies(playerWithTokens)) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        PlayerDetailResponse playerDetailResponse = playerDetailModelAssembler.toModel(playerWithTokens.getPlayer());
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(playerDetailResponse.getId())
            .toUri();
        return ResponseEntity.created(location).body(playerDetailResponse);
    }

    @PostMapping("/password-reset-token")
    public ResponseEntity<Void> sendPasswordResetEmail(@Valid @RequestBody EmailRequest emailRequest) {
        authService.sendPasswordResetEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/password-reset")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        authService.resetPassword(token, passwordChangeRequest.getOldPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email-verification-token")
    public ResponseEntity<Void> sendVerificationEmail(@Valid @RequestBody EmailRequest emailRequest) {
        authService.sendVerificationEmail(emailRequest.getEmail());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/email-verification")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    private List<ResponseCookie> generateCookies(PlayerWithTokens playerWithTokens) {
        List<ResponseCookie> cookies = new ArrayList<>();
        ResponseCookie accessCookie = ResponseCookie.from(COOKIE_ACCESS_TOKEN_NAME, playerWithTokens.getAccessToken())
            .httpOnly(true)
            .secure(true)
            .path("/api")
            .maxAge(Duration.ofMinutes(54))
            .sameSite(SAME_SITE_LAX)
            .build();
        cookies.add(accessCookie);

        ResponseCookie refreshCookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_NAME, playerWithTokens.getRefreshToken())
            .httpOnly(true)
            .secure(true)
            .path("/api/auth")
            .maxAge(Duration.ofDays(30))
            .sameSite(SAME_SITE_LAX)
            .build();
        cookies.add(refreshCookie);

        return cookies;
    }

    private List<ResponseCookie> generateClearCookies() {
        List<ResponseCookie> cookies = new ArrayList<>();
        ResponseCookie accessCookie = ResponseCookie.from(COOKIE_ACCESS_TOKEN_NAME, null)
            .httpOnly(true)
            .secure(true)
            .path("/api")
            .maxAge(0)
            .sameSite(SAME_SITE_LAX)
            .build();
        cookies.add(accessCookie);

        ResponseCookie refreshCookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_NAME, null)
            .httpOnly(true)
            .secure(true)
            .path("/api/auth")
            .maxAge(0)
            .sameSite(SAME_SITE_LAX)
            .build();
        cookies.add(refreshCookie);

        return cookies;
    }

}