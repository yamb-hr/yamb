package com.tejko.yamb.util;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateToken(UUID playerExternalId) {
        return Jwts.builder()
            .setSubject(String.valueOf(playerExternalId))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Optional<UUID> extractIdFromToken(String token) {
        if (validateToken(token)) {
            return Optional.ofNullable(parseToken(token).getBody().getSubject()).map(UUID::fromString);
        } else {
            return Optional.empty();
        }
    }
    

    public Optional<String> extractTokenFromAuthHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .filter(header -> header.startsWith("Bearer "))
            .map(header -> header.substring(7));
    }
}
