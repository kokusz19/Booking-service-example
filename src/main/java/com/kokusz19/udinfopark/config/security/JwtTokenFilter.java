package com.kokusz19.udinfopark.config.security;

import com.kokusz19.udinfopark.api.TokenApi;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenIntrospectResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";

    private final TokenApi tokenApi;

    public JwtTokenFilter(TokenApi tokenApi) {
        this.tokenApi = tokenApi;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isTokenEndpoint(request) || isGetRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractToken(request);

        JwtTokenIntrospectResponse jwtTokenIntrospectResponse;
        try {
            jwtTokenIntrospectResponse = tokenApi.introspectToken(accessToken);
        } catch (Exception e) {
            throw new JwtValidationException("rip", new ArrayList<>());
        }

        if(!jwtTokenIntrospectResponse.isActive()) {
            throw new JwtValidationException("Account of the JWT token is inactive!", new ArrayList<>());
        }

        if(jwtTokenIntrospectResponse.getExpireAt().before(new Date())) {
            throw new JwtValidationException("Jwt token has expired!", new ArrayList<>());
        }
    }

    private boolean isTokenEndpoint(HttpServletRequest request) {
        return "/v1/token".equals(request.getRequestURI());
    }

    private boolean isGetRequest(HttpServletRequest request) {
        return HttpMethod.GET.name().equalsIgnoreCase(request.getMethod());
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            return header.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
