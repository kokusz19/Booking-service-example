package com.kokusz19.udinfopark.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String ALL_PATHS = "**";

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        // TODO: Validate JWT token

        http
                .authorizeHttpRequests(requests -> requests
                        //.requestMatchers(new AntPathRequestMatcher("/token")).permitAll()
                        //.requestMatchers(HttpMethod.GET, ALL_PATHS).permitAll()
                        //.requestMatchers(HttpMethod.POST, ALL_PATHS).authenticated()
                        //.requestMatchers(HttpMethod.PATCH, ALL_PATHS).authenticated()
                        //.requestMatchers(HttpMethod.DELETE, ALL_PATHS).authenticated()
                        //.requestMatchers(HttpMethod.PUT, ALL_PATHS).authenticated()
                        .anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
