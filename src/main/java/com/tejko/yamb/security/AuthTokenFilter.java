package com.tejko.yamb.security;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.util.UUID;

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
import org.springframework.util.StringUtils;
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
        if (isProtectedEndpoint(request.getRequestURI())) {
            System.out.println(request.getRequestURI());
            String authToken = parseToken(request);
            if (authToken != null && jwtUtil.validateToken(authToken)) {
                UUID playerExternalId = jwtUtil.getPlayerExternalIdFromToken(authToken);
                Player player = playerRepo.findByExternalId(playerExternalId).get();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(player, null, player.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.sendError(401, "Unauthorized");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // every endpoint except token fetching and (guest) registration may expect authorization
    private boolean isProtectedEndpoint(String requestURI) {
        String tokenUri = linkTo(methodOn(AuthController.class).getToken(null)).toUri().getPath();
        String registerUri = linkTo(methodOn(AuthController.class).register(null)).toUri().getPath();
        String registerGuestUri = linkTo(methodOn(AuthController.class).registerGuest(null)).toUri().getPath();
        String passwordResetUri = linkTo(methodOn(AuthController.class).sendPasswordResetEmail(null)).toUri().getPath();
        
        if (!requestURI.startsWith("/api")) {
            return false;
        }
    
        return !requestURI.equals(tokenUri) && 
                !requestURI.equals(registerGuestUri) && 
                !requestURI.equals(registerUri) &&
                !requestURI.equals(passwordResetUri);
    }

    private String parseToken(HttpServletRequest request) {
        
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
    
        String tokenParam = request.getParameter("token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }
        return null;
    }
    

}
