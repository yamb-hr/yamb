package com.tejko.components;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.tejko.models.Player;
import com.tejko.repositories.PlayerRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtComponent {

	//@Value("${com.tejko.jwtSecret}")
	private String jwtSecret = "temp";
	
	@Autowired
	PlayerRepository playerRepository;

	public String generateJwt(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		Player player = playerRepository.findByUsername(userDetails.getUsername());

		return Jwts.builder()
			.setSubject(String.valueOf((player.getId())))
			.setIssuedAt(new Date())
			.setExpiration(null)
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
	}

	public String getPlayerIdFromHeader(String headerAuth) {
		String token = headerAuth.substring(7, headerAuth.length());
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwt(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			return true;
		} catch (Exception exception) {
			return false;
		}
	}

}