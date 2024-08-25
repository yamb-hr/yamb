package com.tejko.yamb.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tejko.yamb.domain.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    public String generateToken(Long id) {
        return Jwts.builder()
            .setSubject(id.toString())
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Optional<Long> extractIdFromToken(String token) {
        return Optional.ofNullable(parseToken(token).getBody().getSubject()).map(Long::parseLong);
    }

    public Optional<String> extractTokenFromAuthHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(SecurityConstants.HEADER_AUTHORIZATION))
            .filter(header -> header.startsWith(SecurityConstants.HEADER_AUTHORIZATION_PREFIX))
            .map(header -> header.substring(7));
    }
}
