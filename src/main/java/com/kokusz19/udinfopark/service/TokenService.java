package com.kokusz19.udinfopark.service;

import com.kokusz19.udinfopark.api.TokenApi;
import com.kokusz19.udinfopark.dependency.TestUserRealmKeyCloakFeignClient;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenIntrospectRequest;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenIntrospectResponse;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenRequest;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class TokenService implements TokenApi {

    private static final String KEYCLOAK_CLIENT_ID_NAME="KEYCLOAK_CLIENT_ID";
    private static final String KEYCLOAK_CLIENT_SECRET_NAME="KEYCLOAK_CLIENT_SECRET";
    private final String KEYCLOAK_CLIENT_ID_VALUE;
    private final String KEYCLOAK_CLIENT_SECRET_VALUE;

    private final TestUserRealmKeyCloakFeignClient testUserRealmKeyCloakFeignClient;

    public TokenService(TestUserRealmKeyCloakFeignClient testUserRealmKeyCloakFeignClient) {
        this.testUserRealmKeyCloakFeignClient = testUserRealmKeyCloakFeignClient;

        KEYCLOAK_CLIENT_ID_VALUE = Dotenv.load().get(KEYCLOAK_CLIENT_ID_NAME);
        KEYCLOAK_CLIENT_SECRET_VALUE = Dotenv.load().get(KEYCLOAK_CLIENT_SECRET_NAME);
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

    @Override
    public JwtTokenIntrospectResponse introspectToken(JwtTokenIntrospectRequest jwtTokenIntrospectRequest) {
        MultiValueMap<String, String> input = new LinkedMultiValueMap<>();
        input.add("client_id", jwtTokenIntrospectRequest.clientId());
        input.add("client_secret", jwtTokenIntrospectRequest.clientSecret());
        input.add("token", jwtTokenIntrospectRequest.token());

        return testUserRealmKeyCloakFeignClient.introspectToken(input);
    }

    @Override
    public JwtTokenIntrospectResponse introspectToken(String accessToken) {
        MultiValueMap<String, String> input = new LinkedMultiValueMap<>();
        input.add("client_id", KEYCLOAK_CLIENT_ID_VALUE);
        input.add("client_secret", KEYCLOAK_CLIENT_SECRET_VALUE);
        input.add("token", accessToken);

        return testUserRealmKeyCloakFeignClient.introspectToken(input);
    }
}
