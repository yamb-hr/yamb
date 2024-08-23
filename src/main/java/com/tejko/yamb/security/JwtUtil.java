package com.tejko.yamb.security;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateToken(UUID externalId) {
    return Jwts.builder()
        .setSubject(externalId.toString())
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<UUID> extractExternalIdFromToken(String token) {
        return Optional.ofNullable(parseToken(token).getBody().getSubject().toString())
            .map(UUID::fromString);
    }

    public Optional<String> extractTokenFromAuthHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(SecurityConstants.HEADER_AUTHORIZATION))
            .filter(header -> header.startsWith(SecurityConstants.HEADER_AUTHORIZATION_PREFIX))
            .map(header -> header.substring(7));
    }
}
