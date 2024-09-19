package com.tejko.yamb.security;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejko.yamb.api.controllers.AuthController;
import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.domain.repositories.PlayerRepository;
import com.tejko.yamb.util.JwtUtil;

public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PlayerRepository playerRepo;

    @Autowired
    public AuthTokenFilter(JwtUtil jwtUtil, PlayerRepository playerRepo) {
        this.jwtUtil = jwtUtil;
        this.playerRepo = playerRepo;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (isProtectedEndpoint(requestURI)) {
            extractPlayerFromRequest(request).ifPresent(player -> {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(player, null, player.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedEndpoint(String requestURI) {
        String loginUri = linkTo(methodOn(AuthController.class).getToken(null)).toUri().getPath();
        String guestUri = linkTo(methodOn(AuthController.class).registerGuest(null)).toUri().getPath();
        return !requestURI.equals(loginUri) && !requestURI.equals(guestUri);
    }

    private Optional<Player> extractPlayerFromRequest(HttpServletRequest request) {
        Optional<Player> player = jwtUtil.extractTokenFromAuthHeader(request)
            .flatMap(jwtUtil::extractIdFromToken)
            .flatMap(playerRepo::findByExternalId);
        return player;
    }
}
