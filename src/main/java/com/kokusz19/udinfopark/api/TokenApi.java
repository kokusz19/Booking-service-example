package com.kokusz19.udinfopark.api;

import com.kokusz19.udinfopark.model.dto.security.JwtTokenIntrospectRequest;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenIntrospectResponse;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenRequest;
import com.kokusz19.udinfopark.model.dto.security.JwtTokenResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("token")
public interface TokenApi {

    @PostMapping
    JwtTokenResponse getToken(
            @RequestBody JwtTokenRequest jwtTokenRequest);

    @PostMapping("introspect")
    JwtTokenIntrospectResponse introspectToken(
            @RequestBody JwtTokenIntrospectRequest jwtTokenIntrospectRequest);

    @PostMapping("introspect2")
    JwtTokenIntrospectResponse introspectToken(
            @RequestBody String accessToken);

}
