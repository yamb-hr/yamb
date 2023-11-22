package com.tejko.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.tejko.models.Player;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	//@Value("${com.tejko.jwtSecret}")
	private final String secret = "secret";	

	public String generateToken(Player player) {
		return Jwts.builder()
			.setSubject(String.valueOf((player.getId())))
			.setIssuedAt(new Date())
			.setExpiration(null)
			.signWith(SignatureAlgorithm.HS512, secret)
			.compact();
	}

	public Jws<Claims> parseToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
	}

}