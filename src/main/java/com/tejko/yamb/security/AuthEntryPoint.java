package com.tejko.yamb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.tejko.yamb.util.I18nUtil;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final I18nUtil i18nUtil;

    @Autowired
    public AuthEntryPoint(I18nUtil i18nUtil) {
        this.i18nUtil = i18nUtil;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, i18nUtil.getMessage("error.unauthorized"));
    }
    
}
