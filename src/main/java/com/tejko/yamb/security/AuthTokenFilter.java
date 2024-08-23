package com.tejko.yamb.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejko.yamb.domain.models.Player;
import com.tejko.yamb.interfaces.services.PlayerService;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PlayerService playerService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (isProtectedEndpoint(requestURI)) {
            extractUserFromRequest(request).ifPresent(player -> {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(player, null, player.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedEndpoint(String requestURI) {
        return !"/api/auth/login".equals(requestURI) && !"/api/auth/temp-player".equals(requestURI);
    }

    private Optional<Player> extractUserFromRequest(HttpServletRequest request) {
        return jwtUtil.extractTokenFromAuthHeader(request)
            .flatMap(jwtUtil::extractExternalIdFromToken)
            .flatMap(externalId -> Optional.ofNullable(playerService.getByExternalId(externalId)));
    }
}
