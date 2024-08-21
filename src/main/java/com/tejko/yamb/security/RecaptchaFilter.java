package com.tejko.yamb.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejko.yamb.services.RecaptchaService;

public class RecaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RecaptchaService recaptchaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if ("/api/auth/register".equals(requestURI) || "/api/auth/temp-player".equals(requestURI)) {
            
            String recaptchaToken = request.getHeader("X-Recaptcha-Token");

            if (recaptchaToken == null || !recaptchaService.verifyRecaptcha(recaptchaToken)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}