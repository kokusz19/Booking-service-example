package com.kokusz19.udinfopark.dependency;

import com.kokusz19.udinfopark.model.dto.security.JwtTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "KeyCloakFeignClient", url = "http://localhost:9090", path = "realms/testUserRealm/protocol/openid-connect/token")
public interface TestUserRealmKeyCloakFeignClient {

    @PostMapping(produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    JwtTokenResponse generateTokens(
            @RequestBody MultiValueMap<String, String> jwtTokenRequest);

}
