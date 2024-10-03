package com.tejko.yamb.security;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tejko.yamb.api.controllers.AuthController;
import com.tejko.yamb.business.interfaces.RecaptchaService;

public class RecaptchaFilter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;

    @Autowired
    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (isProtectedEndpoint(request.getRequestURI())) {
            String recaptchaToken = request.getHeader("X-Recaptcha-Token");
            if (recaptchaToken == null || !recaptchaService.verifyRecaptcha(recaptchaToken)) {
                response.sendError(403, "Bots not allowed");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
    
    // recaptcha is only required for (guest) registration
    private boolean isProtectedEndpoint(String requestURI) {
        String registerUri = linkTo(methodOn(AuthController.class).register(null)).toUri().getPath();
        String registerGuestUri = linkTo(methodOn(AuthController.class).registerGuest(null)).toUri().getPath();
        return requestURI.equals(registerUri) || requestURI.equals(registerGuestUri);
    }

}
