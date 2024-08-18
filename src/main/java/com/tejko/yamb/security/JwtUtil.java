package com.tejko.yamb.security;

import java.util.Date;

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

	public String generateToken(String username) {

		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(null)
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
	
	public String extractUsernameFromToken(String token) {
		return parseToken(token).getBody().getSubject();
	}

	public String extractTokenFromAuthHeader(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.HEADER_AUTHORIZATION_PREFIX)) {
			return authorizationHeader.substring(7, authorizationHeader.length());
		}
		return null;
	}

}