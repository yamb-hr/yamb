package com.tejko.yamb.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejko.yamb.business.interfaces.RecaptchaService;

public class RecaptchaFilter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;

    @Autowired
    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (isProtectedEndpoint(request.getRequestURI())) {
            String recaptchaToken = request.getHeader("X-Recaptcha-Token");

            if (recaptchaToken == null || !recaptchaService.verifyRecaptcha(recaptchaToken)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isProtectedEndpoint(String requestURI) {
        return "/api/auth/register".equals(requestURI) || "/api/auth/temp-player".equals(requestURI);
    }
}
