package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.TokenApi;
import com.kokusz19.udinfopark.dependency.TestUserRealmKeyCloakFeignClient;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenRequest;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class TokenService implements TokenApi {

    private final TestUserRealmKeyCloakFeignClient testUserRealmKeyCloakFeignClient;

    public TokenService(TestUserRealmKeyCloakFeignClient testUserRealmKeyCloakFeignClient) {
        this.testUserRealmKeyCloakFeignClient = testUserRealmKeyCloakFeignClient;
    }

    @Override
    public JwtTokenResponse getToken(JwtTokenRequest jwtTokenRequest) {
        MultiValueMap<String, String> input = new LinkedMultiValueMap<>();
        input.add("client_id", jwtTokenRequest.client_id());
        input.add("client_secret", jwtTokenRequest.client_secret());
        input.add("grant_type", jwtTokenRequest.grant_type());
        input.add("username", jwtTokenRequest.username());
        input.add("password", jwtTokenRequest.password());

        return testUserRealmKeyCloakFeignClient.generateTokens(input);
    }
}
