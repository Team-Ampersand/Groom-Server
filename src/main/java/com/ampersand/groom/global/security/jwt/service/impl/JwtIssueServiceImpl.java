package com.ampersand.groom.global.security.jwt.service.impl;

import com.ampersand.groom.domain.member.domain.constant.MemberRole;
import com.ampersand.groom.global.security.jwt.data.TokenDto;
import com.ampersand.groom.global.security.jwt.entity.RefreshTokenRedisEntity;
import com.ampersand.groom.global.security.jwt.repository.RefreshTokenRedisRepository;
import com.ampersand.groom.global.security.jwt.service.JwtIssueService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtIssueServiceImpl implements JwtIssueService {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    @Value("${spring.jwt.access-token.secret}")
    private String accessTokenSecret;
    @Value("${spring.jwt.access-token.expiration}")
    private long accessTokenExpiration;
    @Value("${spring.jwt.refresh-token.secret}")
    private String refreshTokenSecret;
    @Value("${spring.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;
    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    @Override
    public TokenDto issueAccessToken(String email, MemberRole roles) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(accessTokenExpiration);
        return new TokenDto(
                Jwts.builder()
                        .claim("sub", email)
                        .claim("role", roles)
                        .claim("iat", LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .claim("exp", expiration.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .claim("jti", UUID.randomUUID().toString())
                        .signWith(accessTokenKey)
                        .compact(),
                expiration
        );
    }

    @Override
    public TokenDto issueRefreshToken(String email) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(refreshTokenExpiration);
        TokenDto token = new TokenDto(
                Jwts.builder()
                        .claim("sub", email)
                        .claim("iat", LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .claim("exp", expiration.atZone(ZoneId.of("Asia/Seoul")).toEpochSecond())
                        .claim("jti", UUID.randomUUID().toString())
                        .signWith(refreshTokenKey)
                        .compact(),
                expiration
        );
        refreshTokenRedisRepository.save(RefreshTokenRedisEntity.builder()
                .token(token.token())
                .email(email)
                .expiration((long) expiration.getSecond())
                .build()
        );
        return token;
    }
}