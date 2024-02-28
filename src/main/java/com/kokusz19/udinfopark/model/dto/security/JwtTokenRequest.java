package com.kokusz19.udinfopark.model.dto.security;

public record JwtTokenRequest(String client_id, String client_secret, String grant_type, String username, String password) { }
