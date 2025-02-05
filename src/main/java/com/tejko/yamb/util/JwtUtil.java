package com.tejko.yamb.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    public String generateAccessToken(UUID playerExternalId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMs);
        return Jwts.builder()
            .setSubject(playerExternalId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, getSigningKey())
            .compact();
    }

    public String generateRefreshToken(UUID playerExternalId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMs);
        return Jwts.builder()
            .setSubject(playerExternalId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS256, getSigningKey())
            .compact();
    }
    
    public UUID getPlayerExternalIdFromToken(String token) {
        String subject = getClaimsFromToken(token).getBody().getSubject();
        return UUID.fromString(subject);
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("JWT signature cannot be trusted: {}", e.getMessage());
        }
        return false;
    }

    private Jws<Claims> getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

}