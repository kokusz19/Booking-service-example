package com.kokusz19.udinfopark.model.dto.security;

public record JwtTokenIntrospectRequest (String clientId, String clientSecret, String token) { }
