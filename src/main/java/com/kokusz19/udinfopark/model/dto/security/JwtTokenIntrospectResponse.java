package com.kokusz19.udinfopark.model.dto.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

@Getter
public class JwtTokenIntrospectResponse {
    private Date expireAt;
    private Date issuedAt;
    private String username;
    private boolean active;

    @JsonCreator
    public JwtTokenIntrospectResponse(
            @JsonProperty("exp") int exp,
            @JsonProperty("iat") int iat,
            @JsonProperty("username") String username,
            @JsonProperty("active") boolean active
    ) {
        this.expireAt = convertUnixTimestamp(exp);
        this.issuedAt = convertUnixTimestamp(iat);
        this.username = username;
        this.active = active;
    }

    private Date convertUnixTimestamp(int seconds) {
        Date unixStartDate = new Date(70, 0, 1, 0, 0, 0);
        unixStartDate = DateUtils.addMinutes(unixStartDate, -unixStartDate.getTimezoneOffset());
        return DateUtils.addSeconds(unixStartDate, seconds);
    }
}
