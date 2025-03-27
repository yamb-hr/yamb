package com.tejko.yamb.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.StringUtils;
import java.util.List;

public class TokenExtractor {

    private static final String ACCESS_TOKEN_NAME = "token";
    private static final String AUTH_TOKEN_PREFIX = "Bearer ";
    private static final String COOKIE_ACCESS_TOKEN_NAME = "ACCESS_TOKEN";

    public static String extractToken(HttpServletRequest request) {
        System.out.println(request);
        String token = getTokenFromCookie(request);
        if (token == null || token == "") {
            token = getTokenFromAuthHeader(request);
        }
        return token;
    }

    public static String extractToken(ServerHttpRequest request) {
        System.out.println(request);
        String token = getTokenFromCookie(request);
        if (token == null || token == "") {
            token = getTokenFromQueryParam(request);
        }
        return token;
    }

    private static String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_ACCESS_TOKEN_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private static String getTokenFromCookie(ServerHttpRequest request) {
        List<String> cookieHeaders = request.getHeaders().get(HttpHeaders.COOKIE);
        if (cookieHeaders != null) {
            for (String header : cookieHeaders) {
                String[] cookies = header.split(";");
                for (String cookie : cookies) {
                    String[] pair = cookie.split("=");
                    if (pair.length == 2 && pair[0].trim().equals(COOKIE_ACCESS_TOKEN_NAME)) {
                        return pair[1].trim();
                    }
                }
            }
        }
        return null;
    }

    private static String getTokenFromAuthHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(AUTH_TOKEN_PREFIX)) {
            return authHeader.substring(7);
        }
        String tokenParam = request.getParameter(ACCESS_TOKEN_NAME);
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }
        return null;
    }

    private static String getTokenFromQueryParam(ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        if (query != null) {
            String[] params = query.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2 && pair[0].equals(ACCESS_TOKEN_NAME)) {
                    return pair[1];
                }
            }
        }
        return null;
    }
    
}
