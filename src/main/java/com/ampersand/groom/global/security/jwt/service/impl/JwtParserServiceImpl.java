package com.ampersand.groom.global.security.jwt.service.impl;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.security.jwt.service.JwtParserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtParserServiceImpl implements JwtParserService {

    @Value("${spring.jwt.access-token.secret}")
    private String accessTokenSecret;
    @Value("${spring.jwt.refresh-token.secret}")
    private String refreshTokenSecret;
    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    @Override
    public Boolean validateAccessToken(String token) {
        try {
            parseAccessTokenClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean validateRefreshToken(String token) {
        try {
            parseRefreshTokenClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmailFromAccessToken(String token) {
        return parseAccessTokenClaims(token).getSubject();
    }

    @Override
    public MemberRole getRolesFromAccessToken(String token) {
        return MemberRole.valueOf(parseAccessTokenClaims(token).get("role", String.class));
    }

    @Override
    public String getEmailFromRefreshToken(String token) {
        return parseRefreshTokenClaims(token).getSubject();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

    private Claims parseAccessTokenClaims(String token) {
        return Jwts.parser()
                .verifyWith(accessTokenKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims parseRefreshTokenClaims(String token) {
        return Jwts.parser()
                .verifyWith(refreshTokenKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}