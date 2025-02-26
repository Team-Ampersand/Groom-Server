package com.ampersand.groom.global.security.config;


import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration}")
    private long accessTokenExpiration;

    @Value("${spring.jwt.token.refresh-expiration}")
    private long refreshTokenExpiration;

    @Bean
    public SecretKey secretKey() {
        return new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    @Bean
    public long accessTokenExpiration() {
        return accessTokenExpiration;
    }

    @Bean
    public long refreshTokenExpiration() {
        return refreshTokenExpiration;
    }

}
