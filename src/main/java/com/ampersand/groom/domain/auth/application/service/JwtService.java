package com.ampersand.groom.domain.auth.application.service;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String createAccessToken(String email, MemberRole roles) {
        return generateToken(email, accessTokenExpiration, roles);
    }

    public String createRefreshToken(String email, MemberRole roles) {
        String refreshToken = generateToken(email, refreshTokenExpiration, roles);
        redisTemplate.opsForValue().set("refresh_token:" + email, refreshToken, refreshTokenExpiration, TimeUnit.SECONDS);
        return refreshToken;
    }

    public boolean refreshToken(String email, String refreshToken) {
        String storedToken = redisTemplate.opsForValue().get("refresh_token:" + email);

        return storedToken != null && storedToken.equals(refreshToken);
    }

    private String generateToken(String subject, long expirationMs, MemberRole roles) {
        Date now = new Date();
        return Jwts.builder()
                .claim("sub", subject)
                .claim("iat", now.getTime())
                .claim("exp", now.getTime() + expirationMs)
                .claim("role", roles)
                .signWith(secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public MemberRole getRoleFromToken(String token) {
        Claims claims = parseClaims(token);
        String role = claims.get("role", String.class);
        return role != null ? MemberRole.valueOf(role) : null;
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
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }
}
