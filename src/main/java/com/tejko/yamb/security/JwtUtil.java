package com.tejko.yamb.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	//@Value("${com.tejko.yamb.jwtSecret}")
	private final String secret = "secret";	

	public String generateToken(String username) {

		return Jwts.builder()
			.setSubject(username)
			.setIssuedAt(new Date())
			.setExpiration(null)
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
	}

	public boolean validateToken(String token) {
		try {
			parseToken(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}