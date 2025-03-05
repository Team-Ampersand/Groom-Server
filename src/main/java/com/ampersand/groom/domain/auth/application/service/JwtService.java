package com.ampersand.groom.domain.auth.application.service;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SecretKey secretKey;
    @Getter
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public String createAccessToken(String email) {
        return generateToken(email, accessTokenExpiration);
    }

    public String createRefreshToken(String email) {
        String refreshToken = generateToken(email, refreshTokenExpiration);

        redisTemplate.opsForValue().set("refresh_token:" + email, refreshToken, refreshTokenExpiration, TimeUnit.SECONDS);

        return refreshToken;
    }

    public boolean refreshToken(String email, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get("refresh_token:" + email);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return false;
        }
        return true;
    }

    private String generateToken(String subject, long expirationMs) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
}
