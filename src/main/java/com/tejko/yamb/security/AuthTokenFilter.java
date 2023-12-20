package com.tejko.yamb.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejko.yamb.api.services.PlayerService;
import com.tejko.yamb.constants.SecurityConstants;

public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	PlayerService playerService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		UserDetails userDetails = extractUserFromRequest(request);
		if (userDetails != null) {
			try {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}

	// try to extract user from token in auth header then from token cookie
	private UserDetails extractUserFromRequest(HttpServletRequest request) {
		String token = extractTokenFromAuthHeader(request);
		try {
			return playerService.loadUserByUsername(jwtUtil.extractUsernameFromToken(token));
		} catch (Exception e) {
			// ignore
		}
		return null;
	}

	private String extractTokenFromAuthHeader(HttpServletRequest request) {
		String authorizationHeader = request.getHeader(SecurityConstants.HEADER_AUTHORIZATION);
		if (authorizationHeader != null && authorizationHeader.startsWith(SecurityConstants.HEADER_AUTHORIZATION_PREFIX)) {
			return authorizationHeader.substring(7, authorizationHeader.length());
		}
		return null;
	}

}
